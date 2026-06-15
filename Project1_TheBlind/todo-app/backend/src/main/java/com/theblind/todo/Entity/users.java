package com.theblind.todo.Entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    This "users" class is the "user" entity. 
    It is mapped to the "users" table in the database.
*/
@Data // Lombok provides setters, getters, args constructors, ...
@NoArgsConstructor
@Entity
@Table(name = "users") // the table's name 
public class users {
    @Column // not needed b/c of "@Id"
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.UUID) // Auto generate values
    private UUID id;
    private String username;
    private String password;
}
