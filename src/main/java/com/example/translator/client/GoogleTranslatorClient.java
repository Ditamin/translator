package com.example.translator.client;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class GoogleTranslatorClient implements TranslatorClient {
    private final String URL = "https://translate.googleapis.com/translate_a/single?client=gtx&dt=t&sl=%s&tl=%s&q=%s";
    private final RestTemplate restTemplate;

    @Override
    public String fetchWordTranslate(String translateFrom, String translateTo, String word) {
        return restTemplate.getForObject(URL.formatted(translateFrom, translateTo, word), String.class);
    }
}
