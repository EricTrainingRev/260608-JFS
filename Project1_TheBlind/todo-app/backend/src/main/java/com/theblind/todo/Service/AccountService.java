package com.theblind.todo.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.theblind.todo.Entity.User;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepo accountRepository;

    /* 
    * Constructor for AccountService
    * 
    * @param accountRepository - repository to manage Account entities
    */
   /* 
    @Autowired
    public AccountService(AccountRepo accountRepository) {
        this.accountRepository = accountRepository;
    }
        */

    /**
     * usernameValidator - this method checks whether a given username is valid
     * 
     * @param username
     * @return true if username is not blank, does not contain whitespace, 
     *         and is between 5 and 20 characters long (inclusive)
     */
    static private boolean usernameValidator(String username) {
        if (username.length() < 6 || username.length() > 20) return false;

        for (char ch : username.toCharArray()) {
            if (Character.isWhitespace(ch)) return false;
        }

        return true;
    }

    /**
     * passwordValidator - this method checks whether a given password
     * 
     * @param username
     * @return true ifpassword is not blank, does not contain whitespace, 
     *         contains at least two special characters, at least one uppercase
     *         letter, at least one lowercase letter, at least one numeric
     *         character, and is between 6 and 20 characters long (inclusive)
     */
    static private boolean passwordValidator(String password) {
        // return false if length doesnt meet requirements
        if (password.length() < 6 || password.length() > 20) return false;

        // flags for requirements (all false by default)
        boolean specialCharFlag, upperCaseFlag, lowerCaseFlag, numericFlag;
        specialCharFlag = upperCaseFlag = lowerCaseFlag = numericFlag = false;

        // array of special characters, made into a single string for search purposes
        char[] specialChars = {'*', '+', '-', '/', '=', '_', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\'', '<', '>', ',', '.', '?', '~', '`'};
        String charsString = new String(specialChars);
        int specialCharCount = 2;
        
        // for loop - iterates through all chars in password string
        for (char ch : password.toCharArray()) {
            // return false if a character is white space
            if (Character.isWhitespace(ch)) return false;
            
            // if a char meets a requirement, flag is set
            if (!specialCharFlag && (charsString.indexOf(ch) != -1)) {
                specialCharCount--;
                if (specialCharCount <= 0) specialCharFlag = true;
            }
            if (!upperCaseFlag && Character.isUpperCase(ch)) upperCaseFlag = true;
            if (!lowerCaseFlag && Character.isLowerCase(ch)) lowerCaseFlag = true;
            if (!numericFlag && Character.isDigit(ch)) numericFlag = true;  
        }

        // if any flag was not set, return false, otherwise, it is a valid password, return true
        return (specialCharFlag && upperCaseFlag && lowerCaseFlag && numericFlag);
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

        if (usernameValidator(username) && passwordValidator(password)) {
            newAccount = new User(username, password);
            accountRepository.save(newAccount);
        }

        if (newAccount == null) return null;

        return newAccount;
    }
}