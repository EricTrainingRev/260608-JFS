package com.example.demo.controller;

import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

/**
 * Abstract base class for all integration tests in this project.
 *
 * Key responsibilities:
 * - Boots the full Spring application on a random port so tests hit a real server.
 * - Configures REST Assured to target that dynamic port before each test.
 * - Provides a helper method to register + login a user and return a valid JWT,
 *   so subclasses don't have to repeat auth boilerplate in every test.
 *
 * Subclasses simply extend this class and inherit all the setup automatically.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Starts the app on a random available port
@TestPropertySource(locations = "classpath:test.properties") // Uses test-specific config (H2 in-memory DB, etc.)
public abstract class BaseIntegrationTest {

    // Spring injects the actual random port the embedded server started on
    @LocalServerPort
    protected int port;

    /**
     * Runs before EACH test method. Points REST Assured at our running server
     * so all given()/when()/then() chains hit the right host:port.
     */
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    /**
     * Runs once AFTER ALL tests in a subclass finish. Resets REST Assured's
     * static config so it doesn't leak into other test classes.
     */
    @AfterAll
    static void tearDown() {
        RestAssured.reset();
    }

    /**
     * Convenience method that registers a new user and logs them in,
     * returning the JWT token string. This lets test methods quickly
     * obtain a valid auth token without duplicating registration/login logic.
     *
     * @param username the username to register and log in with
     * @param password the password to register and log in with
     * @return the JWT token string returned by the /login endpoint
     */
    protected String obtainAuthToken(String username, String password) {
        // Step 1: Register the user (POST /register expects JSON with username + password)
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", username, "password", password))
        .when()
            .post("/register")
        .then()
            .statusCode(201); // 201 Created = registration succeeded

        // Step 2: Log in and extract the JWT from the response body
        return given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", username, "password", password))
        .when()
            .post("/login")
        .then()
            .statusCode(200) // 200 OK = login succeeded
            .extract()
            .body().asString(); // The response body IS the JWT token (plain string)
    }
}
