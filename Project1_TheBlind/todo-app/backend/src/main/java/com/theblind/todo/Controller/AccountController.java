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
import com.theblind.todo.Service.JWTService;
import com.theblind.todo.Response.LoginResponse;

import java.util.UUID;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AccountController {
    private final AccountService accountService;
    private final JWTService jwtService;

    /**
    *  Constructor for AccountController
    *
    * @param accountService - service to manage business logic 
    *                         of account registration and login.
    */
    @Autowired
    public AccountController(AccountService accountService, JWTService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    /**
    * Endpoint to register a new account
    *
    * @param user - a User object, with a username and password, from the request body
    * @return ResponseEntity object with new user info in body of response (201 CREATED)
    */
    @PostMapping("/register")
    public ResponseEntity<User> createAccount(@RequestBody User user) {
        // if username is null, return 400 error 
        // (will return 401 if not checked in controller)
        if (user.getUsername() == null) {
            throw new RegistrationFailure("Username cannot be null");
        } else if (user.getPassword() == null) {
           throw new RegistrationFailure("Password cannot be null");
        }

        User newUser = accountService.createAccount(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /**
    * Endpoint to have a user login and become authenticated
    *
    * @param user - a User object, with a username and password, from the request body
    * @return ResponseEntity object with existing user info in body of response (200 OK)
    */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginAccount(@RequestBody User user) {
        User existingUser = accountService.loginAccount(user.getUsername(), user.getPassword());
        String jwtToken = jwtService.generateToken(existingUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
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
