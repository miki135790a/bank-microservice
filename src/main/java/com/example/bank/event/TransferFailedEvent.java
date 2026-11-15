package com.example.bank.event;

public record TransferFailedEvent(
        Long operationId,
        String reason
) {}