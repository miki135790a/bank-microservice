package com.example.bank.validation;

import com.example.bank.domain.account.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, Currency> {

    private static final Set<String> ALLOWED = Arrays.stream(Currency.values())
            .map(Enum::name)
            .collect(Collectors.toUnmodifiableSet());

    private static final String ALLOWED_JOINED = String.join(", ", ALLOWED);

    @Override
    public boolean isValid(Currency value, ConstraintValidatorContext context) {
        if (value != null && ALLOWED.contains(value.name())) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "currency must be one of: " + ALLOWED_JOINED
        ).addConstraintViolation();

        return false;
    }
}