package com.example.translator.service;

import com.example.translator.client.TranslatorClient;
import com.example.translator.controller.dto.request.TranslationOptions;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class WordTranslator implements Translator {

    private final TranslatorClient client;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public String translate(TranslationOptions translationOptions) {
        Pattern wordPattern = Pattern.compile("\\p{L}+");
        Matcher wordMatcher = wordPattern.matcher(translationOptions.text());

        List<String> translatedWords = new ArrayList<>();

        while (wordMatcher.find()) {
            translatedWords.add(invokeTranslatorClient(translationOptions, wordMatcher.group()));
        }

        return restoreTextWithTranslation(translationOptions.text(), translatedWords);
    }

    @SneakyThrows
    String invokeTranslatorClient(TranslationOptions translationOptions, String word) {
        return executorService.submit(() -> client.fetchWordTranslate(
                translationOptions.translateFrom(),
                translationOptions.translateTo(),
                word)
        ).get();
    }

    String restoreTextWithTranslation(String sourceText, List<String> words) {
        StringBuilder stringBuilder = new StringBuilder();
        int lastInsertedWordIdx = 0;

        for (int i = 0; i < sourceText.length(); ++i) {
            if (Character.isLetter(sourceText.charAt(i))) {
                stringBuilder.append(words.get(lastInsertedWordIdx++));

                while (i < sourceText.length() && Character.isLetter(sourceText.charAt(i))) {
                    ++i;
                }

                --i;
            } else {
                stringBuilder.append(sourceText.charAt(i));
            }
        }

        return stringBuilder.toString();
    }
}
