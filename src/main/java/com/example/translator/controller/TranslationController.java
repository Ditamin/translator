package com.example.translator.controller;

import com.example.translator.controller.dto.request.TranslateTextRequest;
import com.example.translator.controller.dto.request.TranslationOptions;
import com.example.translator.controller.dto.response.TranslationResponse;
import com.example.translator.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping("/translation")
    TranslationResponse translateText(@RequestBody TranslationOptions translationOptions, HttpServletRequest request) {
        var translateTextRequest = new TranslateTextRequest(translationOptions, request.getRemoteAddr());
        String translation = translationService.getTranslation(translateTextRequest);
        return new TranslationResponse(200, translation);
    }
}
