package com.theblind.todo.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import java.util.*;

@Service
public class AccountService {
    private final AccountRepo accountRepository;

    /* Constructor for AccountService
    *
    * @param accountRepository - repository to manage Account entities
    */
    @Autowired
    public AccountService(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
    }
}