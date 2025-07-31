package com.evrensesi.evrensesi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "user.already.exists.message")
public class UserAlreadyExistsException extends RuntimeException {
    private final String username;
    public UserAlreadyExistsException(String message,String username) {
        super(message);
        this.username = username;
    }
}
