package com.example.bank.web.mapper;

import com.example.bank.domain.operation.Operation;

import com.example.bank.web.dto.response.TransferResponse;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    public TransferResponse toResponse(Operation operation) {
        return new TransferResponse(
                operation.getId(),
                operation.getFromAccount() != null ? operation.getFromAccount().getId() : null,
                operation.getToAccount() != null ? operation.getToAccount().getId() : null,
                operation.getAmount(),
                operation.getType(),
                operation.getStatus(),
                operation.getFailureReason(),
                operation.getCreatedAt()
        );
    }
}