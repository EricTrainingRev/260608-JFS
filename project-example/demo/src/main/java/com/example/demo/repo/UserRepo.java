package com.example.demo.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

/*
    To setup the repository of your entities (remember the entity defines)
    the schema of the table, the repository defines the actions you can
    take on the table

    Remember JpaRepository takes two generics:
    - the entity the repo is for
    - the type of the primary key
*/
public interface UserRepo extends JpaRepository<User, UUID> {
    
}
