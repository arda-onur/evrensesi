package com.arda.evrensesi.exception.customException;

import lombok.Getter;

@Getter
public class StarAlreadyExistsException extends RuntimeException {
    private final Object[] args;
    public StarAlreadyExistsException(String message,Object... args) {
        super(message);
        this.args = args;
    }
}
