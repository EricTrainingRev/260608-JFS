package com.example.demo.repo;

import com.example.demo.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
    To setup the repository of your entities (remember the entity defines)
    the schema of the table, the repository defines the actions you can
    take on the table

    Remember JpaRepository takes two generics:
    - the entity the repo is for
    - the type of the primary key
*/
@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
  // We don't have to tell Spring how to write/implement this query: it just knows how
  Optional<User> findByUsername(String username);
  // Note we are returning an optional type above: this is a convinient way to check if a resource
  // has been found or not
}
