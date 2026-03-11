package com.arda.evrensesi.exception;


import com.arda.evrensesi.exception.customException.StarAlreadyExistsException;
import com.arda.evrensesi.exception.customException.StarCreationException;
import com.arda.evrensesi.exception.customException.UserRegistrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<String> handleUserRegistrationException(UserRegistrationException ex, Locale locale) {
        log.warn("User registration error: key={}, args={}", ex.getMessage(), ex.getArgs());

        String msg = messageSource.getMessage(ex.getMessage(), ex.getArgs(), ex.getMessage(), locale);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }
    @ExceptionHandler(StarAlreadyExistsException.class)
    public ResponseEntity<String> handleStarAlreadyExistsException(StarAlreadyExistsException ex, Locale locale) {
        log.warn("Star conflict occurred: messageKey={}", ex.getMessage());

        String msg = messageSource.getMessage(ex.getMessage(), ex.getArgs(), ex.getMessage(), locale);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }

    @ExceptionHandler(StarCreationException.class)
    public ResponseEntity<String> handleStarCreationException(StarCreationException ex, Locale locale) {
        log.warn("Star creation failed: messageKey={}", ex.getMessage());

        String msg = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(msg);
    }



    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleLoginAuthExceptions(AuthenticationException ex, Locale locale) {
        log.warn("Login failed: {}", ex.getClass().getSimpleName());

        String messageKey;

        if (ex instanceof BadCredentialsException) {
            messageKey = "auth.bad.credentials";
        } else if (ex instanceof UsernameNotFoundException) {
            messageKey = "auth.user.not.found";
        } else {
            messageKey = "auth.failed";
        }

        String msg = messageSource.getMessage(messageKey, null, locale);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(msg);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, Locale locale) {

        log.warn("Illegal argument: key={}", ex.getMessage());

        String msg = messageSource.getMessage(ex.getMessage(), null, ex.getMessage(), locale);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodNotValidArgumentException(MethodArgumentNotValidException ex,
                                                                                                   Locale locale) {
        String key = ex.getBindingResult().getFieldError().getDefaultMessage();

        String msg = messageSource.getMessage(key, null, key, locale);

        log.warn("Validation error: {}", key);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
    }
}
