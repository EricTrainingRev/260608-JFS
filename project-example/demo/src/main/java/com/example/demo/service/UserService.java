package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.exception.RegistrationFailure;
import com.example.demo.repo.UserRepo;

import lombok.RequiredArgsConstructor;

// this tells lombok to make a constructor that initializes all final fields
@RequiredArgsConstructor
@Service
public class UserService {
    
    // make sure to make your repo final
    private final UserRepo userRepo;

    /*
        NOTE: in modern Spring the default way Spring will handle Dependency Injection is through
        constructor injection. This requires a single constructor, which we can provide via Lombok's
        RequiredArgsConstructor decorator. To make sure all the fields we need to be injected are actually
        provided a value through the constructor we must make sure to mark the fields as final
    */

    /*

        We are starting with implementing the registration feature, so here in the User service we can validate
        the rules of registering a new user are being followed. To keep this simple we will make just a few
        checks:
        - Username
            - between 5-15 character
            - unique
            - not null
        - Passwords
            - between 5-15 characters
            - inlcudes uppercase, lowercase, and numeric characters
            - not null

        if these conditions are met then the registration should go through, otherwise it should be
        rejected. Note the overlap between the checks: this process is an excellent candidate to be broken
        down into smaller functions to facilitate the process

        NOTE: these checks besides the unique check could all be handled clientside, but having them here as well
              will help make your application more fault-tolerant and reduce the risk of any catastrophic failure
              in your application
    */


    // NOTE: the controller handles putting together the response, so if this method executes without any
    //       exceptions being thrown the controller will assume success and return the appropriate response
    public void registerUser(User user){
        // we have our helper methods, we will call them here

        // first we can check the username is not null
        if(isNull(user.getUsername())){
            throw new RegistrationFailure("Username should not be empty");
        }
        // then we check it is the correct length
        if(!isCorrectLength(user.getUsername())){
            throw new RegistrationFailure("Username should be between 5 and 15 characters");
        }
        // finally we check it is unique against the database
        if(!isUnique(user.getUsername())){
            throw new RegistrationFailure("Username must be unique");
        }

        // next we check the password

        // first we can check it is not null
        if(isNull(user.getPassword())){
            throw new RegistrationFailure("Password should not be empty");
        }

        // then we can check it is the correct length
        if(!isCorrectLength(user.getPassword())){
            throw new RegistrationFailure("Password should be between 5 and 15 characters");
        }

        // finally we can check the proper characters are in the password
        if(!hasRequiredCharacters(user.getPassword())){
            throw new RegistrationFailure("Password requires all special characters");
        }

        // if we have reached this point then we know the new credentials are valid and we can register the
        // user
        userRepo.save(user);
    }

    /*
        There are multiple ways you could set up your helper validation methods: they can go in the service
        class itself, into a utility class, really whatever you can think of. Whatever decision you make, the
        most important thing you can do is be consistent in your approach.
    */
    public boolean isCorrectLength(String credential){
        return 5 <= credential.length() && credential.length() <= 15;
    }

    public boolean isNull(String credential){
        return credential == null;
    }

    public boolean hasRequiredCharacters(String credential) {
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;

        for (char c : credential.toCharArray()) {
            if (Character.isLowerCase(c)) hasLowerCase = true;
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLowerCase && hasUpperCase && hasDigit) return true;
        }
        return false;
    }

    public boolean isUnique(String credential){
        Optional<User> userOptional = userRepo.findByUsername(credential);
        // NOTE: we want isPresent to return false, because that means the username is actually unique, hence the "not" operator
        return !userOptional.isPresent();
    }


}
