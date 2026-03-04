package com.arda.evrensesi.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private MessageSource messageSource;

        @ExceptionHandler(UserRegistrationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
         public String handleUserRegistrationException(UserRegistrationException ex, Locale locale){
            return messageSource.getMessage(ex.getMessage(), ex.getArgs(),ex.getMessage(),locale);
         }
}
