package com.arda.evrensesi.exception.customException;

import lombok.Getter;

@Getter
public class StarCreationException extends RuntimeException {

    private final Object[] args;
    public StarCreationException(String message, Object... args) {
        super(message);
        this.args = args;
    }
}
