package com.example.bank.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClientRequest (
    @NotBlank(message = "Full name must not be blank")
    @Size(max = 255, message = "FullName must not exceed 255 characters")
    String fullName,

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email
) {}
