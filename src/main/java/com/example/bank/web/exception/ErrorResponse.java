package com.example.bank.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse (
        String code,
        String message,
        List <FieldErrorDetail> fields
) {
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code,message,List.of());
    }

    public static ErrorResponse validation(List<FieldErrorDetail> fields) {
        return new ErrorResponse("VALIDATION_ERROR","Validation failed",fields);

    }
}
