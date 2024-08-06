package com.example.translator.client;

public interface TranslatorClient {
    String fetchWordTranslate(String translateFrom, String translateTo, String word);
}
