package com.example.demo.entity;

import java.util.UUID;

import lombok.Data;

/*
    This User class is going to be my User entity: whenever  user data is utilized in my application this class
    will be used to represent it. We need to make sure that all relevant data for our users is present  to
    start. At a minimum we need a username and password field to represent the actual username and password
    the user creates. Thinking ahead, usernames tend to be unique so we could have the username act as our
    primary key if we did not want to make a unique field for the id.
*/
@Data // tells Lombok to provide setters, getters, required args constructor, and more
public class User {
    private UUID id;
    private String username;
    private String password;
}
