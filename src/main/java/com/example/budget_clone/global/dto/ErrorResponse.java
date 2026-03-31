package com.example.budget_clone.global.dto;

public record ErrorResponse (boolean success, ErrorDetail error) {

    public static ErrorResponse of (String code, String message) {
        return new ErrorResponse(false, new ErrorDetail(code, message));
    }

    public record ErrorDetail(String code, String message) {}
}
