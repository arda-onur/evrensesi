package com.arda.evrensesi.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private MessageSource messageSource;

        @ExceptionHandler(UserRegistrationException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
         public String handleUserRegistrationException(UserRegistrationException ex, Locale locale){
            log.warn("User registration error: key={}, args={}", ex.getMessage(), ex.getArgs());
            return messageSource.getMessage(ex.getMessage(), ex.getArgs(),ex.getMessage(),locale);
         }
}
