package com.evrensesi.evrensesi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Comments not exist!")
public class NoSuchCommentException extends RuntimeException {
    public NoSuchCommentException(String message) {
        super(message);
    }
}
