package com.example.translator.serviceTest;

import com.example.translator.client.MicrosoftTranslatorClient;
import com.example.translator.client.TranslatorClient;
import com.example.translator.controller.dto.request.TranslationOptions;
import com.example.translator.service.WordTranslator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class restoringTextTest {
    TranslatorClient client = Mockito.mock(MicrosoftTranslatorClient.class);

    WordTranslator translator = new WordTranslator(client);

    @Test
    void threeWordsTest() {
        String from = "en";
        String to = "ru";
        String text = "Hello! Whats up?";
        TranslationOptions options = new TranslationOptions(text, from, to);
        Mockito.when(client.fetchWordTranslate(from, to, "Hello")).thenReturn("Привет");
        Mockito.when(client.fetchWordTranslate(from, to, "Whats")).thenReturn("Что");
        Mockito.when(client.fetchWordTranslate(from, to, "up")).thenReturn("вверх");

        String translation = translator.translate(options);
        Assertions.assertThat(translation).isEqualTo("Привет! Что вверх?");
    }

    @Test
    void restoringTextWithDifferentSymbolsTest() {
        String from = "ru";
        String to = "en";
        String text = "1,2.3! Привет?!_";
        TranslationOptions options = new TranslationOptions(text, from, to);
        Mockito.when(client.fetchWordTranslate(from, to, "Привет")).thenReturn("Hello");

        String translation = translator.translate(options);
        Assertions.assertThat(translation).isEqualTo("1,2.3! Hello?!_");
    }

    @Test
    void textWithoutLettersTest() {
        String from = "ru";
        String to = "en";
        String text = "192.102.0.1";
        TranslationOptions options = new TranslationOptions(text, from, to);

        String translation = translator.translate(options);
        Assertions.assertThat(translation).isEqualTo(text);
    }
}
