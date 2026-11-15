package com.example.bank.web.dto.response;

import java.time.LocalDateTime;

public record ClientResponse (
        Long id,
        String fullName,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
