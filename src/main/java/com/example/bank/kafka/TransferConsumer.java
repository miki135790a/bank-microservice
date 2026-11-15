package com.example.bank.kafka;
import com.example.bank.domain.account.Account;
import com.example.bank.domain.operation.Operation;
import com.example.bank.domain.operation.OperationStatus;
import com.example.bank.event.TransferInitiatedEvent;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.TransferRepository;
import com.example.bank.web.exception.InsufficientFundsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransferConsumer {


    private final TransferRepository operationRepository;
    private final AccountRepository accountRepository;


    @KafkaListener(topics = "transfer-initiated", groupId = "bank-group")
    @Transactional
    public void onTransferInitiated(TransferInitiatedEvent event) {
        log.info("Received TransferInitiated: {}", event);


        Operation operation = operationRepository.findById(event.operationId())
                .orElseThrow(() -> new EntityNotFoundException("Operation not found: " + event.operationId()));

        if (operation.getStatus() != OperationStatus.PENDING) {
            log.warn("Operation {} already processed. Skipping.", event.operationId());
            return;
        }

        try {

            Long firstId = Math.min(event.fromAccountId(), event.toAccountId());
            Long secondId = Math.max(event.fromAccountId(), event.toAccountId());

            Account first = accountRepository.findByIdForUpdate(firstId)
                    .orElseThrow(() -> new EntityNotFoundException("Account " + firstId + " not found"));
            Account second = accountRepository.findByIdForUpdate(secondId)
                    .orElseThrow(() -> new EntityNotFoundException("Account " + secondId + " not found"));


            Account from = Objects.equals(first.getId(), event.fromAccountId()) ? first : second;
            Account to = Objects.equals(first.getId(), event.toAccountId()) ? first : second;


            from.checkActive();
            to.checkActive();

            if (from.getCurrency() != to.getCurrency()) {
                throw new IllegalStateException(
                        "Currency mismatch: " + from.getCurrency() + " -> " + to.getCurrency());
            }

            from.debit(event.amount());
            to.credit(event.amount());

            operation.markCompleted();
            log.info("Transfer completed: operationId={}", event.operationId());

        } catch (InsufficientFundsException | IllegalStateException e) {
            log.warn("Business error: {}", e.getMessage());
            operation.markFailed(e.getMessage());
        } catch (Exception e) {
            log.error("Technical error. Will retry then DLQ.", e);
            throw e;
        }
    }
}