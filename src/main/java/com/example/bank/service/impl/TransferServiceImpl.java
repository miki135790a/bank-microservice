package com.example.bank.service.impl;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.operation.Operation;
import com.example.bank.domain.outbox.OutboxEvent;
import com.example.bank.event.TransferInitiatedEvent;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.OutboxRepository;
import com.example.bank.repository.TransferRepository;
import com.example.bank.service.TransferService;
import com.example.bank.web.dto.request.TransferRequest;
import com.example.bank.web.dto.response.TransferResponse;
import com.example.bank.web.mapper.TransferMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final TransferMapper transferMapper;

    @Override
    @Transactional(readOnly = true)
    public TransferResponse findById(Long id) {
        Operation operation = transferRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Operation with id " + id + " not found"));
        return transferMapper.toResponse(operation);
    }

    @Override
    @Transactional
    public TransferResponse initiateTransfer(TransferRequest request) {
        if (Objects.equals(request.fromAccountId(), request.toAccountId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // 2. Загружаем счета (без блокировки — блокировка будет в Consumer'е)
        Account from = accountRepository.findById(request.fromAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + request.fromAccountId() + " not found"));
        Account to = accountRepository.findById(request.toAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + request.toAccountId() + " not found"));

        Operation operation = Operation.createTransfer(from, to, request.amount());
        Operation saved = transferRepository.save(operation);
        TransferInitiatedEvent event = new TransferInitiatedEvent(
                saved.getId(),
                from.getId(),
                to.getId(),
                request.amount()
        );

        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateType("OPERATION")
                    .aggregateId(saved.getId().toString())
                    .eventType("transfer-initiated")
                    .payload(objectMapper.writeValueAsString(event))
                    .build();
            outboxRepository.save(outboxEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }

        return new TransferResponse(
                saved.getId(),
                from.getId(),
                to.getId(),
                request.amount(),
                saved.getType(),
                saved.getStatus(),
                saved.getFailureReason(),
                saved.getCreatedAt()
        );



    }




}
