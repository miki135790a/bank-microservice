package com.example.bank.service.impl;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.client.Client;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.ClientRepository;
import com.example.bank.service.AccountService;
import com.example.bank.web.dto.request.CreateAccountRequest;
import com.example.bank.web.dto.response.AccountResponse;
import com.example.bank.web.mapper.AccountMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse create(Long clientId, CreateAccountRequest request) {
        Client client = findClientById(clientId);
        Account account = accountMapper.toEntity(client, request);
        Account saved = accountRepository.save(account);
        return accountMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> findAllByClientId(Long clientId) {
        findClientById(clientId);
        return accountRepository.findAllByClientId(clientId).stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AccountResponse closeAccount(Long clientId, Long accountId) {
        Account account = findAccountById(accountId);
        validateOwnership(clientId,accountId,account);
        account.close();
        return accountMapper.toResponse(account);
    }



    private Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client with id " + id + " not found"));
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account with id " + id + " not found"));
    }

    private void validateOwnership (Long clientId, Long accountId, Account account) {
        if (!Objects.equals(account.getClient().getId(), clientId)) {
            throw new EntityNotFoundException(
                    "Account with id " + accountId + " not found for client " + clientId
            );
        }
    }
}