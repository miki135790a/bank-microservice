package com.example.bank.web.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotNull(message = "FromAccountId must not be null")
        Long fromAccountId,

        @NotNull(message = "ToAccountId must not be null")
        Long toAccountId,

        @NotNull(message = "Amount must not be null")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount
) {}