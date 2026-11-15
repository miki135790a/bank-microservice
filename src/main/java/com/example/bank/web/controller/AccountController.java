package com.example.bank.web.controller;


import com.example.bank.service.AccountService;
import com.example.bank.web.dto.request.CreateAccountRequest;
import com.example.bank.web.dto.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients/{clientId}/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public List <AccountResponse> getAll (@PathVariable Long clientId){
        return accountService.findAllByClientId(clientId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create (@PathVariable Long clientId, @Valid @RequestBody CreateAccountRequest request) {
        return accountService.create(clientId, request);
    }

    @PostMapping("/{accountId}/close")
    public AccountResponse close (@PathVariable Long clientId, @PathVariable Long accountId){
        return accountService.closeAccount(clientId,accountId);
    }




}
