package com.example.bank.web.mapper;

import com.example.bank.domain.account.Account;
import com.example.bank.domain.client.Client;
import com.example.bank.web.dto.request.CreateAccountRequest;
import com.example.bank.web.dto.response.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(Client client, CreateAccountRequest request) {
        return Account.create(client, request.number(), request.currency());
    }

    public AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getClient().getId(),
                account.getNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.getClosedAt()
        );
    }
}