package com.example.demo.exception;

public class BookValidationException extends RuntimeException {
    public BookValidationException(String message){
        super(message);
    }
}
