package com.example.demo.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.exception.RegistrationFailure;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    // Reminder: because Spring is managing our dependencies, we only need to declare the particular component
    // needed for this class to work. In this case, the userService
    private final UserService userService;

    /*
        there is a fair bit going on here:
        - @PostMapping("/register") -> this tells Spring we want to expose a "register" endpoint clients can
                                       consume, and when they do it should trigger the associated method
        - @RequestBody -> this tells Spring to use Jackson to convert the request body JSON into, in this case,
                          a User object. If there are unexpected properties in the body that do not match the
                          User class an exception will be thrown, and any missing properties for the object will
                          simply not be initilazed in the object
    */
    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@RequestBody User user){
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /*
        NOTE: this exception handler is NOT universal: it will only trigger for any RegistrationFailiure
        exceptions triggered by UserController code
    */
    @ExceptionHandler(RegistrationFailure.class)
    public ResponseEntity<String> handleRegistrationFailure(RegistrationFailure exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.error("Data integrity violation during registration", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Could not complete registration: data conflict");
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleResourceFailure(DataAccessResourceFailureException exception) {
        log.error("Database resource failure during registration", exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service temporarily unavailable, please try again later");
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<String> handleQueryTimeout(QueryTimeoutException exception) {
        log.error("Query timeout during registration", exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Request timed out, please try again later");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleGenericDataAccess(DataAccessException exception) {
        log.error("Unexpected data access error during registration", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration");
    }

}
