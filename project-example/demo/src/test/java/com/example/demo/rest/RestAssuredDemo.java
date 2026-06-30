package com.example.demo.rest;

// The REST Assured team recommends including these static imports for ease of framework use
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.entity.User;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/*
    In order to get our test class configured properly we need to add a few configurations
    to our class:
    - @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT): this tells Spring
      to actually spin up the web server so we can use it for testing while it listens on a random port
    - @TestPropertySource(locations = "classpath:test.properties"): this tells Spring when this test class is run to
      use the test.properties configuration which will override any properties in the default application.properties
*/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class RestAssuredDemo {
    
  // Make sure you save the random port the web server is running on in a variable
  @LocalServerPort
  private int port;

  @BeforeEach
  void setup(){
    // we can specify where we want all of our requests to be sent
    RestAssured.baseURI = "http://localhost";
    // we can specify the port for the requests
    RestAssured.port = port;
  }

  @Test
  void simpleRestAssuredTest(){

    // test data
    User testUser = new User();
    testUser.setUsername("Username");
    testUser.setPassword("P0ssword");

    /*
      Rest Assured tests have a three part struture that follows a given/when/then pattern.
      - given() = this method gives us the tools to put together our http request
      - when() = this method gives us access to the tools to actually make the request
      - then() = this method gives us access to the response and lets us validate it
    */

    // the given() let's us set up the headers, body, cookies, any sort of pre-conditions needed for us to make the request
    given()
      .contentType(ContentType.JSON)
      .body(testUser)
    // the when() is where we decide what type of request we are actually making and the endpoint we are hitting
    .when()
      .post("/register")
    // the then() is where we perform our validation
    .then()
      .statusCode(201);
  }

}
