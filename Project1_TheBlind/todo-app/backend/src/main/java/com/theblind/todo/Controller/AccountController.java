package com.theblind.todo.Controller;

import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    /* Constructor for AccountController
    *
    * @param accountService - service to manage business logic 
    *                         of account registration and login.
    */
    @PostMapping("/auth/register")
    public ResponseEntity<User> createAccount(@RequestBody String username, @RequestBody String password) {
        return ResponseEntity.ok(accountService.createAccount(username, password));
    }
}
