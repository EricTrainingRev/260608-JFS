package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for UserController endpoints (registration and login).
 *
 * These tests verify:
 *   - Successful registration with valid credentials
 *   - Registration validation (duplicate username, null fields, password rules, length)
 *   - Successful login returning a JWT
 *   - Login failure scenarios (wrong password, non-existent user, null fields)
 *
 * Each test uses unique usernames to avoid cross-test interference.
 * Extends BaseIntegrationTest for REST Assured config and helpers.
 */
class UserControllerIT extends BaseIntegrationTest {

    // ==================== Registration Tests (Requirements 4.1–4.5) ====================

    /**
     * Happy path: valid username (5-15 chars) + valid password (has uppercase,
     * lowercase, and digit) should return 201 with an empty body.
     */
    @Test
    void register_withValidCredentials_returns201() {
        given()
            .log().all() // tells REST Assured to log all the request data
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser1", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .log().all() // tells REST Assured to log all of the response data
            .statusCode(201)               // 201 Created = account registered
            .body(emptyOrNullString());    // No body content on successful registration
    }

    /**
     * Usernames must be unique. Registering the same username twice should
     * return 400 with an error message on the second attempt.
     */
    @Test
    void register_withDuplicateUsername_returns400() {
        // First registration — should succeed
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser2", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Second registration with same username — should fail
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser2", "password", "Pass2def"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Username must be unique"));
    }

    /**
     * A null username should be caught by validation and return 400.
     * Note: We use HashMap here because Map.of() doesn't allow null values.
     */
    @Test
    void register_withNullUsername_returns400() {
        Map<String, String> body = new HashMap<>();
        body.put("username", null);      // Explicitly null
        body.put("password", "Pass1abc");

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Username should not be empty"));
    }

    /**
     * A null password should be caught by validation and return 400.
     * Same HashMap trick since Map.of() throws NPE on null values.
     */
    @Test
    void register_withNullPassword_returns400() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "regUser4");
        body.put("password", null);      // Explicitly null

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Password should not be empty"));
    }

    /**
     * Password must contain at least one uppercase letter, one lowercase letter,
     * and one digit. "abcdefg" has only lowercase — should be rejected.
     */
    @Test
    void register_withInvalidPasswordCharacters_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser5", "password", "abcdefg"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Password requires all special characters"));
    }

    /**
     * Both username and password must be between 5 and 15 characters.
     * "ab" is only 2 chars — below the minimum length of 5.
     */
    @Test
    void register_withCredentialsTooShort_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "ab", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("should be between 5 and 15 characters"));
    }

    /**
     * Username exceeding 15 characters should also be rejected.
     * "thisUsernameIsWayTooLong" is well over the 15-char limit.
     */
    @Test
    void register_withCredentialsTooLong_returns400() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "thisUsernameIsWayTooLong", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("should be between 5 and 15 characters"));
    }

    // ==================== Login Tests (Requirements 5.1–5.5) ====================

    /**
     * Happy path: after registering, logging in with correct credentials
     * should return 200 with a JWT in the response body.
     * A valid JWT has exactly 3 Base64URL-encoded segments separated by dots.
     */
    @Test
    void login_withValidCredentials_returns200WithJwt() {
        // Register the user first
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser1", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Log in and capture the response body (the JWT token)
        String token = given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser1", "password", "Pass1abc"))
        .when()
            .post("/login")
        .then()
            .statusCode(200)   // 200 OK = login succeeded
            .extract()
            .body().asString();

        // Verify the token has the standard JWT structure: header.payload.signature
        String[] segments = token.split("\\.");
        assertEquals(3, segments.length, "JWT should have 3 dot-separated segments");
    }

    /**
     * Logging in with the wrong password (user exists but password doesn't match)
     * should return 401 Unauthorized with a generic error message.
     * We use a generic message to avoid revealing whether the username exists.
     */
    @Test
    void login_withWrongPassword_returns401() {
        // Register a user first
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser2", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Attempt login with incorrect password
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser2", "password", "WrongPass1"))
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body(containsString("Invalid username or password"));
    }

    /**
     * Logging in with a username that was never registered should return 401.
     * Same generic error message as wrong-password to prevent username enumeration.
     */
    @Test
    void login_withNonExistentUser_returns401() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "NoSuchUser1", "password", "Pass1abc"))
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body(containsString("Invalid username or password"));
    }

    /**
     * Sending null username and password to login should return 401.
     * The service layer checks for null fields before attempting authentication.
     */
    @Test
    void login_withNullFields_returns401() {
        Map<String, String> body = new HashMap<>();
        body.put("username", null);
        body.put("password", null);

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body(containsString("Username and password are required"));
    }
}
