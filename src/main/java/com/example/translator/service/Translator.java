package com.example.translator.service;

import com.example.translator.controller.dto.request.TranslationOptions;

public interface Translator {
    String translate(TranslationOptions translationOptions);
}
