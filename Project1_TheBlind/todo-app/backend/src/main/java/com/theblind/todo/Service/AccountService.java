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

    /* 
    * Constructor for AccountService
    * 
    * @param accountRepository - repository to manage Account entities
    */
    @Autowired
    public AccountService(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * usernameValidater - this method checks whether a given username is valid
     * 
     * @param username
     * @return true if username is not blank, does not contain whitespace, 
     *         and is between 5 and 20 characters long (inclusive)
     */
    private boolean usernameValidater(String username) {
        if (username.length() < 5 || username.length() > 20) return false;

        for (char ch : username.toCharArray()) {
            if (Character.isWhitespace(ch)) return false;
        }

        return true;
    }

    /*  
    * createAccount - Creates a new account with the given username and password
    *
    * @param username - the username for the new account
    * @param password - the password for the new account
    * @return the newly created Account object

     */
    public User createAccount(String username, String password) {
        User newAccount = null;
        Optional<User> existingUser = accountRepository.findByUsername(username);

        if (existingUser.isPresent()) return null;

        if (usernameValidater(username)) {
            newAccount = new User(username, password);
            accountRepository.save(newAccount);
        }

        if (newAccount == null) return null;

        return newAccount;
    }
}