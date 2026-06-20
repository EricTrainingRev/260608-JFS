package com.theblind.todo.Exception;

public class LoginFailureException extends RuntimeException {
    /**
    * LoginFailureException - a custom exception to be thrown when login credentials don't match any users
    *
    * @param message - a string containing a message describing the error
    */
    public LoginFailureException(String message) {
        super(message);
    }
}
