package com.example.demo.exception;

public class LoginFailure extends RuntimeException {
    public LoginFailure(String message) {
        super(message);
    }
}
