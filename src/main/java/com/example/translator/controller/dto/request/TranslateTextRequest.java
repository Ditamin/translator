package com.example.translator.controller.dto.request;

public record TranslateTextRequest(
        TranslationOptions options,
        String ip
) {
}
