package com.evrensesi.evrensesi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Username or Email already exist!")
public class UserAlreadyExistsException extends RuntimeException {
    private final String username;
    public UserAlreadyExistsException(String message,String username) {
        super(message);
        this.username = username;
    }
}
