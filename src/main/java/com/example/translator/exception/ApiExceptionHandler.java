package com.example.translator.exception;

import com.example.translator.controller.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handle(InvalidSourceLangException e) {
        return new ApiErrorResponse(400, "Не найден язык исходного сообщения");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handle(InvalidTargetLangException e) {
        return new ApiErrorResponse(400, "Не найден указанный язык для перевода");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handle(QuotaRequestExceededException e) {
        return new ApiErrorResponse(400, "Превышен лимит бесплатных запросов");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiErrorResponse handle(ResourceAccessException e) {
        return new ApiErrorResponse(400, "Ошибка доступа к ресурсу перевода");
    }
}
