package com.example.bank.web.dto.response;

import com.example.bank.domain.account.AccountStatus;
import com.example.bank.domain.account.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
    Long id,
    Long clientId,
    String number,
    BigDecimal balance,
    Currency currency,
    AccountStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    LocalDateTime closedAt

)

{}

