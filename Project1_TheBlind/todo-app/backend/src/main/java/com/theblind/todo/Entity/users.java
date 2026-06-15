package com.theblind.todo.Entity;

import java.util.UUID;

import lombok.Data;

/*
    This "users" class is the "user" entity. 
    It is mapped to the "users" table in the database.
*/
@Data // Lombok provides setters, getters, args constructors, ...
public class users {
    private UUID id;
    private String username;
    private String password;
}
