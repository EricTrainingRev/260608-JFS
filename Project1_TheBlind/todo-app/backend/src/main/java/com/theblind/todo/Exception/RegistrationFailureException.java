package com.theblind.todo.Exception;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class RegistrationFailureException extends RuntimeException {
    /**
    * RegistrationFailure - a custom exception to be thrown when user registration fails
    *
    * @param message - a string containing a message describing the error
    */
    public RegistrationFailureException(String message) {
        super(message);
    }
}
