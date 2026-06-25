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
 * Integration tests for the AuthInterceptor — the Spring interceptor that
 * validates JWT tokens on protected endpoints (like /books).
 *
 * These tests verify that the interceptor correctly rejects requests with:
 *   - Expired tokens
 *   - Malformed Authorization headers (wrong prefix, missing entirely)
 *   - Tokens signed with a different secret key
 * ...and allows requests with valid tokens through.
 *
 * We manually craft JWTs here (using JJWT) so we can control expiration, signing
 * keys, etc. without depending on the app's login flow for invalid-token scenarios.
 */
class AuthInterceptorIT extends BaseIntegrationTest {

    // The signing secret that matches what the application uses (must stay in sync with app config)
    private static final String CORRECT_SECRET = "this-works-for-dev-use-environemnt-in-prod";

    // A different secret — tokens signed with this will fail verification
    private static final String WRONG_SECRET = "a-completely-different-secret-key-here!";

    // ==================== Auth Interceptor Tests (Requirements 10.1–10.5) ====================

    /**
     * Verifies that a token whose expiration date is in the past gets rejected.
     * The interceptor should respond with 401 and an "Invalid or expired token" message.
     */
    @Test
    void request_withExpiredToken_returns401() {
        // Build an HMAC signing key from the correct secret
        SecretKey key = Keys.hmacShaKeyFor(CORRECT_SECRET.getBytes(StandardCharsets.UTF_8));

        // Craft a JWT that expired 1 hour ago (issuedAt = 24h ago, expiration = 1h ago)
        String expiredToken = Jwts.builder()
                .subject(UUID.randomUUID().toString())        // random user ID as subject
                .claim("username", "testuser")                // custom claim
                .issuedAt(new Date(System.currentTimeMillis() - 86400000))   // issued 24 hours ago
                .expiration(new Date(System.currentTimeMillis() - 3600000))  // expired 1 hour ago
                .signWith(key)                                               // signed with valid secret
                .compact();

        // Send a request to a protected endpoint with the expired token
        given()
            .header("Authorization", "Bearer " + expiredToken)
        .when()
            .get("/books")
        .then()
            .statusCode(401) // Should be rejected
            .body(containsString("Invalid or expired token"));
    }

    /**
     * Verifies that an Authorization header that doesn't start with "Bearer " is rejected.
     * The interceptor checks the header format before even trying to parse the token.
     */
    @Test
    void request_withMalformedAuthHeader_returns401() {
        // "NotBearer" prefix instead of "Bearer" — interceptor should catch this immediately
        given()
            .header("Authorization", "NotBearer some-token-value")
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Missing or malformed Authorization header"));
    }

    /**
     * Verifies that a request with NO Authorization header at all is rejected.
     * Protected endpoints require the header to be present.
     */
    @Test
    void request_withNoAuthHeader_returns401() {
        // No Authorization header included in the request
        given()
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Missing or malformed Authorization header"));
    }

    /**
     * Verifies that a token signed with a DIFFERENT secret key is rejected.
     * Even though the token structure is valid, the signature won't match
     * what the server expects, so it should fail verification.
     */
    @Test
    void request_withWrongSecretToken_returns401() {
        // Sign the token with a completely different key than what the server uses
        SecretKey wrongKey = Keys.hmacShaKeyFor(WRONG_SECRET.getBytes(StandardCharsets.UTF_8));
        String wrongSecretToken = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .claim("username", "testuser")
                .issuedAt(new Date())                                          // issued now
                .expiration(new Date(System.currentTimeMillis() + 86400000))   // expires in 24h (not expired)
                .signWith(wrongKey)                                             // signed with WRONG secret
                .compact();

        // Server should reject this because the signature doesn't match its key
        given()
            .header("Authorization", "Bearer " + wrongSecretToken)
        .when()
            .get("/books")
        .then()
            .statusCode(401)
            .body(containsString("Invalid or expired token"));
    }

    /**
     * Verifies that a legitimately obtained token (via register + login) passes
     * through the interceptor successfully. We just confirm we DON'T get a 401.
     */
    @Test
    void request_withValidToken_doesNotReturn401() {
        // Use the helper from BaseIntegrationTest to get a real, valid JWT
        String token = obtainAuthToken("authUser1", "Pass1abc");

        // Hit a protected endpoint — should NOT be 401 (likely 200 with empty book list)
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(not(equalTo(401))); // Any status other than 401 means auth passed
    }
}
