package com.theblind.todo.Controller;

import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import com.theblind.todo.Exception.RegistrationFailure;
import com.theblind.todo.Exception.GlobalExceptionHandler;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {
    private final AccountService accountService;

    /**
    *  Constructor for AccountController
    *
    * @param accountService - service to manage business logic 
    *                         of account registration and login.
    */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
    * Endpoint to register a new account
    *
    * @param user - a User object, with a username and password, from the request body
    * @return ResponseEntity object with new user info in body of response
    */
    @PostMapping("auth/register")
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        // if username is null, return 400 error 
        // (will return 401 if not checked in controller)
        // (can't check for null in entity because it's not a requirement for the database but is a requirement for the api)
        if (user.getUsername() == null) {
            List<Map.Entry<String, Boolean>> invalidRequirementList = List.of();
            throw new RegistrationFailure("Username cannot be null", invalidRequirementList);
        } else if (user.getPassword() == null) {
            List<Map.Entry<String, Boolean>> invalidRequirementList = List.of();
            throw new RegistrationFailure("Password cannot be null", invalidRequirementList);
        }
        User newUser = accountService.createAccount(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /** 
    * Exception handler for RegistrationFailure exceptions in this controller.
    *
    * @param exception - a RegistrationFailure exception (user inputted bad username or password)
    * @return a response entity with a message and a 400 status code
    */
    @ExceptionHandler(RegistrationFailure.class)
    public ResponseEntity<String> handleRegistrationFailure(RegistrationFailure exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
