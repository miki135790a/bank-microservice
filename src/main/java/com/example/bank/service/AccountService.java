package com.example.bank.service;

import com.example.bank.web.dto.request.CreateAccountRequest;
import com.example.bank.web.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse create(Long clientId, CreateAccountRequest request);
    List<AccountResponse> findAllByClientId(Long clientId);
    AccountResponse closeAccount(Long clientId, Long accountId);

}
