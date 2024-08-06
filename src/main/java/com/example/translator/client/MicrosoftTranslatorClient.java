package com.example.translator.client;

import com.example.translator.client.dto.microsoft.MicrosoftError;
import com.example.translator.client.dto.microsoft.MicrosoftErrorResponse;
import com.example.translator.client.dto.microsoft.MicrosoftResponse;
import com.example.translator.exception.InvalidSourceLangException;
import com.example.translator.exception.InvalidTargetLangException;
import com.example.translator.exception.QuotaRequestExceededException;
import com.example.translator.exception.ResourceAccessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MicrosoftTranslatorClient implements TranslatorClient {
    private final RestTemplate restTemplate;
    @Value("${client.microsoft.apikey}")
    private String API_KEY;
    @Setter
    private String url = "https://microsoft-translator-text.p.rapidapi.com/translate?from=%s&to=%s&api-version=3.0&profanityAction=NoAction&textType=plain";

    @Override
    @Retryable(value = ResourceAccessException.class, backoff = @Backoff(delay = 1000, multiplier = 2))
    public String fetchWordTranslate(String translateFrom, String translateTo, String word) {
        String uri = url.formatted(translateFrom, translateTo);
        String body = "[{\"Text\":\"%s\"}]".formatted(word);
        boolean isWasLower = Character.isLowerCase(word.charAt(0));

        RequestEntity<String> request = formRequest(uri, body);
        ResponseEntity<MicrosoftResponse[]> response = null;

        try {
            response = restTemplate.exchange(request, MicrosoftResponse[].class);
        } catch (HttpClientErrorException e) {
            errorHandler(e.getResponseBodyAs(MicrosoftErrorResponse.class).error());
        }

        String translatedWord = getTranslatedWordFromResponse(response);
        return isWasLower ? translatedWord.toLowerCase() : translatedWord;
    }

    RequestEntity<String> formRequest(String uri, String body) {
        return RequestEntity.post(uri)
                .header("x-rapidapi-host", "microsoft-translator-text.p.rapidapi.com")
                .header("x-rapidapi-key", API_KEY)
                .header("Content-Type", "application/json")
                .body(body);
    }

    String getTranslatedWordFromResponse(ResponseEntity<MicrosoftResponse[]> response) {
        return Objects.requireNonNull(response.getBody())[0].translations().getFirst().text();
    }

    void errorHandler(MicrosoftError error) {
        switch (error.code()) {
            case 400035 -> throw new InvalidSourceLangException();
            case 400036 -> throw new InvalidTargetLangException();
            case 403000 -> throw new QuotaRequestExceededException();
            default -> throw new ResourceAccessException();
        }
    }
}
