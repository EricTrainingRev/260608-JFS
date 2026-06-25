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
 * Integration tests for UserController endpoints.
 * Extends BaseIntegrationTest for REST Assured configuration and auth helpers.
 */
class UserControllerIT extends BaseIntegrationTest {

    // ==================== Registration Tests (Requirements 4.1–4.5) ====================

    @Test
    void register_withValidCredentials_returns201() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser1", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201)
            .body(emptyOrNullString());
    }

    @Test
    void register_withDuplicateUsername_returns400() {
        // Register the first time
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser2", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Attempt duplicate registration
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser2", "password", "Pass2def"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Username must be unique"));
    }

    @Test
    void register_withNullUsername_returns400() {
        Map<String, String> body = new HashMap<>();
        body.put("username", null);
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

    @Test
    void register_withNullPassword_returns400() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "regUser4");
        body.put("password", null);

        given()
            .contentType(ContentType.JSON)
            .body(body)
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Password should not be empty"));
    }

    @Test
    void register_withInvalidPasswordCharacters_returns400() {
        // Password has no uppercase or digit — only lowercase
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "regUser5", "password", "abcdefg"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("Password requires all special characters"));
    }

    @Test
    void register_withCredentialsTooShort_returns400() {
        // Username too short (under 5 chars)
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "ab", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(400)
            .body(containsString("should be between 5 and 15 characters"));
    }

    @Test
    void register_withCredentialsTooLong_returns400() {
        // Username too long (over 15 chars)
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

    @Test
    void login_withValidCredentials_returns200WithJwt() {
        // Register a user first
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser1", "password", "Pass1abc"))
        .when()
            .post("/register")
        .then()
            .statusCode(201);

        // Login and verify JWT is returned
        String token = given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser1", "password", "Pass1abc"))
        .when()
            .post("/login")
        .then()
            .statusCode(200)
            .extract()
            .body().asString();

        // JWT format: 3 dot-separated Base64URL segments
        String[] segments = token.split("\\.");
        assertEquals(3, segments.length, "JWT should have 3 dot-separated segments");
    }

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

        // Attempt login with wrong password
        given()
            .contentType(ContentType.JSON)
            .body(Map.of("username", "LoginUser2", "password", "WrongPass1"))
        .when()
            .post("/login")
        .then()
            .statusCode(401)
            .body(containsString("Invalid username or password"));
    }

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
