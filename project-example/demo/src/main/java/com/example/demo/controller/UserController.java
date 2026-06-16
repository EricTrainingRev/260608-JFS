package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    // Reminder: because Spring is managing our dependencies, we only need to declare the particular component
    // needed for this class to work. In this case, the userService
    private final UserService userService;

    /*
        there is a fair bit going on here:
        - @PostMapping("/register") -> this tells Spring we want to expose a "register" endpoint clients can
                                       consume, and when they do it should trigger the associated method
        - @RequestBody -> this tells Spring to use Jackson to convert the request body JSON into, in this case,
                          a User object. If there are unexpected properties in the body that do not match the
                          User class an exception will be thrown, and any missing properties for the object will
                          simply not be initilazed in the object
    */
    @PostMapping("/register")
    public ResponseEntity<Void> registerNewUser(@RequestBody User user){
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

}
