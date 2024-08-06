package com.example.translator.repository.jdbc;

import com.example.translator.repository.TranslationRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class JdbcTranslationRepository implements TranslationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveTranslation(String ip, String sourceText, String translatedText) {
        jdbcTemplate.update(
                "INSERT INTO note (ip, sourceText, translated) VALUES (?, ?, ?)",
                ip,
                sourceText,
                translatedText
        );
    }
}
