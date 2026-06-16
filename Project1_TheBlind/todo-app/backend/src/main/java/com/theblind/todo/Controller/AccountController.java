package com.theblind.todo.Controller;

import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import com.theblind.todo.Exception.RegistrationFailure;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;

    /* Constructor for AccountController
    *
    * @param accountService - service to manage business logic 
    *                         of account registration and login.
    */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /* Endpoint to register a new account
    *
    * @param user - a User object, with a username and password
    * @return N/A
    */
    @PostMapping("/auth/register")
    public ResponseEntity<Void> createAccount(@RequestBody User user) {
        accountService.createAccount(user.getUsername(), user.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /* Exception handler for RegistrationFailure exceptions in this controller.
    *
    * @param Registration - a User object, with a username and password
    * @return a response entity with a message
    *         and HttpStatus.CONFLICT status code
    */
    @ExceptionHandler(RegistrationFailure.class)
    public ResponseEntity<String> handleRegistrationFailure(RegistrationFailure exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
