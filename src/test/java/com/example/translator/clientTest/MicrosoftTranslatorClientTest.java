package com.example.translator.clientTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.example.translator.client.MicrosoftTranslatorClient;
import com.example.translator.exception.InvalidSourceLangException;
import com.example.translator.exception.InvalidTargetLangException;
import com.example.translator.exception.QuotaRequestExceededException;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@WireMockTest(httpPort = 8081)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MicrosoftTranslatorClientTest {
    RestTemplate restTemplate = new RestTemplate();
    MicrosoftTranslatorClient client = new MicrosoftTranslatorClient(restTemplate);

    @BeforeAll
    public void setPort() {
        client.setUrl("http://localhost:8081");
    }

    @Test
    public void correctResponseDeserializationTest() {
        String translateFrom = "en";
        String translateTo = "ru";
        String word = "hello";

        String responseBody = """
                [
                    {
                        "translations": [
                            {
                                "text": "Привет",
                                "to": "ru"
                            }
                        ]
                    }
                ]""";

        stubFor(post(anyUrl())
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        String translation = client.fetchWordTranslate(translateFrom, translateTo, word);

        Assertions.assertThat(translation).isEqualTo("привет");
    }

    @Test
    public void invalidTargetLanguageTest() {
        String translateFrom = "en";
        String translateTo = "a";
        String word = "hello";

        String responseBody = "{\"error\":{\"code\":400036,\"message\":\"The target language is not valid.\"}}";
        stubFor(post(anyUrl())
                .willReturn(badRequest()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        Assertions.assertThatThrownBy(
                () -> client.fetchWordTranslate(translateFrom, translateTo, word)
        ).isInstanceOf(InvalidTargetLangException.class);
    }

    @Test
    public void invalidSourceLanguageTest() {
        String translateFrom = "a";
        String translateTo = "ru";
        String word = "hello";

        String responseBody = "{\"error\":{\"code\":400035,\"message\":\"The source language is not valid.\"}}";
        stubFor(post(anyUrl())
                .willReturn(badRequest()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        Assertions.assertThatThrownBy(
                () -> client.fetchWordTranslate(translateFrom, translateTo, word)
        ).isInstanceOf(InvalidSourceLangException.class);
    }

    @Test
    public void quotaRequestExceededTest() {
        String translateFrom = "en";
        String translateTo = "ru";
        String word = "hello";

        String responseBody = "{\"error\":{\"code\":403000,\"message\":\"The subscription has exceeded its free quota.\"}}";
        stubFor(post(anyUrl())
                .willReturn(badRequest()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        Assertions.assertThatThrownBy(
                () -> client.fetchWordTranslate(translateFrom, translateTo, word)
        ).isInstanceOf(QuotaRequestExceededException.class);
    }

    @Test
    void incorrectSourceLang() {
        String translateFrom = "en";
        String translateTo = "ru";
        String word = "Привет";

        String responseBody = """
                [
                    {
                        "translations": [
                            {
                                "text": "Привет",
                                "to": "ru"
                            }
                        ]
                    }
                ]""";;

        stubFor(post(anyUrl())
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseBody)));

        String translation = client.fetchWordTranslate(translateFrom, translateTo, word);

        Assertions.assertThat(translation).isEqualTo("Привет");
    }
}