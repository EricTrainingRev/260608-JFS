package com.theblind.todo.Controller;

import org.springframework.web.bind.annotation.*;
import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class AccountController {
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
}
