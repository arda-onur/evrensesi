package com.arda.evrensesi.exception;

import lombok.Getter;

@Getter
public class UserRegistrationException extends RuntimeException {

  private final Object[] args;

  public UserRegistrationException(String message, Object... args) {
    super(message);
    this.args = args;
  }

}
