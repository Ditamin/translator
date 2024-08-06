package com.example.translator.controller.dto.response;

public record ApiErrorResponse(
        int code,
        String message
) {
}
