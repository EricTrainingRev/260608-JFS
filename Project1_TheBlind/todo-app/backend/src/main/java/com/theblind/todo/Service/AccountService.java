package com.theblind.todo.Service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.theblind.todo.Entity.User;
import com.theblind.todo.Exception.RegistrationFailure;
import com.theblind.todo.Repo.AccountRepo;
import com.theblind.todo.Service.AccountService;
import java.util.*;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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
     *         is not a duplicate, and is between 5 and 15 characters long (inclusive)
     */
    private boolean usernameValidator(String username) {
        // flags for requirements (all false by default)
        boolean lengthFlag, noWhiteSpaceFlag, notNullFlag, uniqueFlag;
        lengthFlag = noWhiteSpaceFlag = uniqueFlag = false;
        noWhiteSpaceFlag = true;
        notNullFlag = (username != null);

        Optional<User> existingUser = accountRepository.findByUsername(username);
        uniqueFlag = !existingUser.isPresent();

        if (username.length() >= 5 && username.length() <= 15) lengthFlag = true;

        for (char ch : username.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                noWhiteSpaceFlag = false;
                break;
            }
        }

        if (noWhiteSpaceFlag && lengthFlag && notNullFlag && uniqueFlag) return true;

        String genericMessage = "Username is not valid";
        List<Map.Entry<String, Boolean>> invalidRequirementList = List.of(
            Map.entry("Must be between 5 and 15 characters long (inclusive)", lengthFlag),
            Map.entry("Must not contain whitespace", noWhiteSpaceFlag),
            Map.entry("Must not be null", notNullFlag),
            Map.entry("Must be unique (username already exists)", uniqueFlag)
        );

        throw new RegistrationFailure(genericMessage, invalidRequirementList);
    }

    /**
     * passwordValidator - this method checks whether a given password
     * 
     * @param username
     * @return true if password is not null, does not contain whitespace, 
     *         contains at least two special characters, at least one uppercase
     *         letter, at least one lowercase letter, at least one numeric
     *         character, and is between 5 and 15 characters long (inclusive)
     */
    private boolean passwordValidator(String password) {
        // flags for requirements (all false by default)
        boolean specialCharFlag, upperCaseFlag, lowerCaseFlag, numericFlag, lengthFlag, noWhiteSpaceFlag, notNullFlag;
        specialCharFlag = upperCaseFlag = lowerCaseFlag = numericFlag = lengthFlag = false;
        noWhiteSpaceFlag = true;

        notNullFlag = (password != null);

        if (password.length() >= 5 && password.length() <= 15) lengthFlag = true;

        // array of special characters, made into a single string for search purposes
        final char[] SPECIAL_CHARS = {'*', '+', '-', '/', '=', '_', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\'', '<', '>', ',', '.', '?', '~', '`'};
        String specialCharsString = new String(SPECIAL_CHARS);
        int specialCharCount = 2;
        
        // for loop - iterates through all chars in password string
        for (char ch : password.toCharArray()) {
            // return false if a character is white space
            if (!noWhiteSpaceFlag && Character.isWhitespace(ch)) noWhiteSpaceFlag = false;
            
            // if a char meets a requirement, flag is set
            if (!specialCharFlag && (specialCharsString.indexOf(ch) != -1)) {
                specialCharCount--;
                if (specialCharCount <= 0) specialCharFlag = true;
            }
            if (!upperCaseFlag && Character.isUpperCase(ch)) upperCaseFlag = true;
            if (!lowerCaseFlag && Character.isLowerCase(ch)) lowerCaseFlag = true;
            if (!numericFlag && Character.isDigit(ch)) numericFlag = true;  
        }

        // if any flag was not set, give exception, otherwise, it is a valid password, return true
        if (specialCharFlag && upperCaseFlag && lowerCaseFlag && numericFlag) return true;

        String genericMessage = "Password is not valid";
        List<Map.Entry<String, Boolean>> invalidRequirementList = List.of(
            Map.entry("At least two special characters", specialCharFlag),
            Map.entry("At least one uppercase letter", upperCaseFlag),
            Map.entry("At least one lowercase letter", lowerCaseFlag),
            Map.entry("At least one numeric character", numericFlag),
            Map.entry("Must be between 5 and 15 characters long (inclusive)", lengthFlag),
            Map.entry("Must not contain whitespace", noWhiteSpaceFlag),
            Map.entry("Must not be null", notNullFlag)
        );

        throw new RegistrationFailure(genericMessage, invalidRequirementList);
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

        if (usernameValidator(username) && passwordValidator(password)) {
            newAccount = new User(username, password);
            accountRepository.save(newAccount);
        }

        if (newAccount == null) return null;

        return newAccount;
    }
}