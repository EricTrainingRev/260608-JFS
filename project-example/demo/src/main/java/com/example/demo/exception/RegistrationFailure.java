package com.example.demo.exception;

public class RegistrationFailure extends RuntimeException {
  public RegistrationFailure(String message) {
    super(message);
  }
}
