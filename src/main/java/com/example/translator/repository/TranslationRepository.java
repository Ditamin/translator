package com.example.translator.repository;

import org.springframework.transaction.annotation.Transactional;

public interface TranslationRepository {
    @Transactional
    void saveTranslation(String ip, String sourceText, String translatedText);
}
