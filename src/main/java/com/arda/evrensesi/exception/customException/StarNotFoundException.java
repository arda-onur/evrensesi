package com.arda.evrensesi.exception.customException;

import lombok.Getter;

@Getter
public class StarNotFoundException extends RuntimeException {

  Object[] args;
  public StarNotFoundException(String message, Object... args) {
    super(message);
    this.args = args;
  }
}
