package com.example.demo.service;

import org.springframework.stereotype.Service;

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

}
