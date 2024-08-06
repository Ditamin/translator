package com.example.translator.service;

import com.example.translator.controller.dto.request.TranslateTextRequest;
import com.example.translator.repository.TranslationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final Translator translator;

    public String getTranslation(TranslateTextRequest request) {
        String translatedText = translator.translate(request.options());
        translationRepository.saveTranslation(request.ip(), request.options().text(), translatedText);
        return translatedText;
    }
}
