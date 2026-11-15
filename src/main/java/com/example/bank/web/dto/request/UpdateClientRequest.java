package com.example.bank.web.dto.request;

import jakarta.validation.constraints.*;

public record UpdateClientRequest (

        @Pattern(regexp = ".*\\S.*")
        @Size(min = 1, max = 255, message = "FullName must not exceed 255 characters")
        String fullName,


        @Email(message = "Email must be valid")
        @Size(min = 1, max = 255, message = "Email must not exceed 255 characters")
        String email
) {}

