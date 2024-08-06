package com.example.translator.client.dto.microsoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MicrosoftResponse(
    @JsonProperty("translations")
    List<Translation> translations
) {
}