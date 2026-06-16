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

    /**
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

        return newAccount;
    }

    /**
     * usernameValidator - this method checks whether a given username is valid
     * 
     * @param username
     * @return true if username is not blank, does not contain whitespace, 
     *         is not a duplicate, and is between 5 and 15 characters long (inclusive)
    */
    private boolean usernameValidator(String username) {
        // flags for requirements
        boolean lengthFlag = isCorrectLength(username), noWhiteSpaceFlag = !hasWhiteSpace(username), 
            notNullFlag = !isNull(username), uniqueFlag = isUnique(username);


        // if all flags are set, valid username
        if (notNullFlag && uniqueFlag && noWhiteSpaceFlag && lengthFlag) return true;

        // if not, return error
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
     * @param password - a string
     * @return true if password is not null, does not contain whitespace, 
     *         contains at least two special characters, at least one uppercase
     *         letter, at least one lowercase letter, at least one digit, 
     *         and is between 5 and 15 characters long (inclusive)
    */
    private boolean passwordValidator(String password) {
        // flags for requirements
        boolean charsFlag = hasRequiredCharacters(password), lengthFlag = isCorrectLength(password), 
            noWhiteSpaceFlag = !hasWhiteSpace(password), notNullFlag = !isNull(password);

        // if all flags set, valid password
        if (notNullFlag && noWhiteSpaceFlag && charsFlag && lengthFlag) return true;

        // if not, return error
        String genericMessage = "Password is not valid";
        List<Map.Entry<String, Boolean>> invalidRequirementList = List.of(
            Map.entry("Must contain at least one uppercase, one lowercase, one numeric, and two special characters", charsFlag),
            Map.entry("Must be between 5 and 15 characters long (inclusive)", lengthFlag),
            Map.entry("Must not contain whitespace", noWhiteSpaceFlag),
            Map.entry("Must not be null", notNullFlag)
        );

        throw new RegistrationFailure(genericMessage, invalidRequirementList);
    }

    // check if credential is already in database
    public boolean isUnique(String credential) { return !accountRepository.findByUsername(credential).isPresent(); }

    // check if length is between 5 and 15 (inclusive)
    public boolean isCorrectLength(String credential){
        return 5 <= credential.length() && credential.length() <= 15;
    }

    // check if null
    public boolean isNull(String credential){
        return credential == null;
    }

    // check if no whitespace
    public boolean hasWhiteSpace(String credential) {
        for (char ch : credential.toCharArray()) {
            if(Character.isWhitespace(ch)) return true;
        }

        return false;
    }

    // check if there is at least one uppercase and one lowercase letters, at least one digit, and if there are at least two special characters
    public boolean hasRequiredCharacters(String credential) {
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChars = false;

        // list of special chars, casted into string for search purposes
        final char[] SPECIAL_CHARS = {'*', '+', '-', '/', '=', '_', '{', '}', '[', ']', '|', '\\', ':', ';', '"', '\'', '<', '>', ',', '.', '?', '~', '`'};
        String specialCharsString = new String(SPECIAL_CHARS);
        // at least two special characters must be found
        int specialCharCount = 2;

        for (char ch : credential.toCharArray()) {
            if (!hasLowerCase && Character.isLowerCase(ch)) hasLowerCase = true;
            if (!hasUpperCase && Character.isUpperCase(ch)) hasUpperCase = true;
            if (!hasDigit && Character.isDigit(ch)) hasDigit = true;
            if (!hasSpecialChars && (specialCharsString.indexOf(ch) != -1)) {
                specialCharCount--;
                if (specialCharCount <= 0) hasSpecialChars = true;
            }
            // if all requirements are met, return true
            if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialChars) return true;
        }

        // if at least one requirement is missing, return false
        return false;
    }
}