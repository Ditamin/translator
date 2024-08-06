package com.example.translator.controller.dto.request;

import jakarta.validation.constraints.Size;

public record TranslationOptions(
        @Size(min = 1, max = 200)
        String text,
        String translateFrom,
        String translateTo
) { }
