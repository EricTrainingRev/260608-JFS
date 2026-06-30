package com.example.demo.rest;

// The REST Assured team recommends including these static imports for ease of framework use
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.springframework.boot.test.context.SpringBootTest;

/*
    In order to get our test class configured properly we need to add a few configurations
    to our class:
    - @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT): this tells Spring
      to actually spin up the web server so we can use it for testing while it listens on a random port
*/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestAssuredDemo {
    
}
