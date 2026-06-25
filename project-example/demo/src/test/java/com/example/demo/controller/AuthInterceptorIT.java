package com.example.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration tests for AuthInterceptor JWT validation.
 * Verifies that requests with missing, malformed, expired, or wrong-secret tokens
 * are rejected with 401, and valid tokens pass through.
 */
class AuthInterceptorIT extends BaseIntegrationTest {

    private static final String CORRECT_SECRET = "this-works-for-dev-use-environemnt-in-prod";
    private static final String WRONG_SECRET = "a-completely-different-secret-key-here!";

    // ==================== Auth Interceptor Tests (Requirements 10.1–10.5) ====================

    @Test
    void request_withExpiredToken_returns401() {
        SecretKey key = Keys.hmacShaKeyFor(CORRECT_SECRET.getBytes(StandardCharsets.UTF_8));
        String expiredToken = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("username", "testuser")
                .issuedAt(new Date(System.currentTimeMillis() - 86400000))
                .expiration(new Date(System.currentTimeMillis() - 3600000)) // expired 1 hour ago
                .signWith(key)
                .compact();

        given()
            .header("Authorization", "Bearer " + expiredToken)
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Invalid or expired token"));
    }

    @Test
    void request_withMalformedAuthHeader_returns401() {
        given()
            .header("Authorization", "NotBearer some-token-value")
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Missing or malformed Authorization header"));
    }

    @Test
    void request_withNoAuthHeader_returns401() {
        given()
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Missing or malformed Authorization header"));
    }

    @Test
    void request_withWrongSecretToken_returns401() {
        SecretKey wrongKey = Keys.hmacShaKeyFor(WRONG_SECRET.getBytes(StandardCharsets.UTF_8));
        String wrongSecretToken = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("username", "testuser")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(wrongKey)
                .compact();

        given()
            .header("Authorization", "Bearer " + wrongSecretToken)
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Invalid or expired token"));
    }

    @Test
    void request_withValidToken_doesNotReturn401() {
        String token = obtainAuthToken("authUser1", "Pass1abc");

        int statusCode = given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(not(equalTo(401)))
            .extract()
            .statusCode();
    }
}
