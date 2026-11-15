package com.example.bank.event;

import java.math.BigDecimal;

public record TransferCompletedEvent(
        Long operationId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount
) {}