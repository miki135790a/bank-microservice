package com.example.bank.web.dto.request;

import com.example.bank.domain.account.Currency;
import com.example.bank.validation.ValidCurrency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(
        @NotNull (message = "Number must not be null")
        @Pattern(regexp = "\\d{20}", message = "Account number must be exactly 20 digits")
        String number,

        @ValidCurrency
        Currency currency
)
{}
