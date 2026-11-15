package com.example.bank.event;

import java.math.BigDecimal;

public record TransferInitiatedEvent(
        Long operationId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount
) {}