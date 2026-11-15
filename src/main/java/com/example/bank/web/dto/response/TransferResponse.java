package com.example.bank.web.dto.response;

import com.example.bank.domain.operation.OperationStatus;
import com.example.bank.domain.operation.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long OperationId,
        Long fromAccountId,
        Long toAccountId,
        BigDecimal amount,
        OperationType type,
        OperationStatus status,
        String failureReason,
        LocalDateTime createdAt
) {}