package com.example.demo.controller;

import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for BookController endpoints (CRUD operations on books).
 *
 * Each test registers a UNIQUE user so book collections don't interfere across tests.
 * The tests verify:
 *   - Creating books with valid/invalid data
 *   - Reading books (all, by ID, across users)
 *   - Updating books with valid/invalid data
 *   - Deleting books
 *   - Proper 401 responses when auth is missing
 *
 * Extends BaseIntegrationTest for REST Assured config and the obtainAuthToken() helper.
 */
class BookControllerIT extends BaseIntegrationTest {

    // ==================== Book Creation Tests (Requirements 6.1–6.5) ====================

    /**
     * Happy path: creating a book with all valid fields returns 201
     * and the response body contains the saved book with a generated ID.
     */
    @Test
    void createBook_withValidData_returns201WithBook() {
        String token = obtainAuthToken("bookUser1", "Pass1abc");

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "The Great Gatsby",
                "author", "F. Scott Fitzgerald",
                "genre", "Fiction",
                "pageCount", 180
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)                                    // 201 Created
            .body("id", notNullValue())                         // Server generated a UUID
            .body("title", equalTo("The Great Gatsby"))         // Echoes back what we sent
            .body("author", equalTo("F. Scott Fitzgerald"))
            .body("genre", equalTo("Fiction"))
            .body("pageCount", equalTo(180));
    }

    /**
     * Attempting to create a book WITHOUT an Authorization header should be blocked
     * by the AuthInterceptor before the controller is even reached.
     */
    @Test
    void createBook_withoutAuth_returns401() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "title", "Some Book",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(401); // Interceptor rejects unauthenticated requests
    }

    /**
     * A garbage token (not a valid JWT at all) should also be rejected with 401.
     */
    @Test
    void createBook_withInvalidToken_returns401() {
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer invalid.garbage.token")
            .body(Map.of(
                "title", "Some Book",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(401); // Token can't be parsed/verified
    }

    /**
     * Validation: a book with a blank title should be rejected with 400.
     * The service layer enforces that title is non-empty.
     */
    @Test
    void createBook_withBlankTitle_returns400() {
        String token = obtainAuthToken("bookUser2", "Pass1abc");

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "",           // Blank title — should fail validation
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(400); // Bad Request due to validation failure
    }

    /**
     * Validation: pageCount must be between 1 and 25000.
     * Tests both below-minimum (0) and above-maximum (25001) values.
     */
    @Test
    void createBook_withInvalidPageCount_returns400() {
        String token = obtainAuthToken("bookUser3", "Pass1abc");

        // pageCount = 0 (below minimum of 1)
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Valid Title",
                "author", "Valid Author",
                "genre", "Fiction",
                "pageCount", 0
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(400);

        // pageCount = 25001 (above maximum of 25000)
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Valid Title",
                "author", "Valid Author",
                "genre", "Fiction",
                "pageCount", 25001
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(400);
    }

    // ==================== Book Read Tests (Requirements 7.1–7.7) ====================

    /**
     * After creating two books for a user, GET /books should return exactly
     * those two books in a JSON array, each with all expected fields populated.
     */
    @Test
    void getBooks_withBooks_returns200WithArray() {
        String token = obtainAuthToken("readUser1", "Pass1abc");

        // Create two books for this user
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Book One",
                "author", "Author One",
                "genre", "Fiction",
                "pageCount", 100
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Book Two",
                "author", "Author Two",
                "genre", "Non-Fiction",
                "pageCount", 250
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201);

        // GET /books should return an array of exactly 2 books belonging to this user
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(200)
            .body("$", hasSize(2))              // Root JSON array has 2 elements
            .body("[0].id", notNullValue())     // First book has an ID
            .body("[0].title", notNullValue())  // First book has a title
            .body("[0].author", notNullValue()) // First book has an author
            .body("[0].pageCount", notNullValue()); // First book has a page count
    }

    /**
     * A freshly registered user with no books should get back an empty JSON array.
     */
    @Test
    void getBooks_withNoBooks_returns200WithEmptyArray() {
        String token = obtainAuthToken("readUser2", "Pass1abc");

        // Fresh user, no books created — should return []
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(200)
            .body("$", empty()); // Empty JSON array
    }

    /**
     * GET /books/{id} with a valid book ID should return that specific book
     * with all its fields matching what was originally created.
     */
    @Test
    void getBookById_withValidId_returns200WithBook() {
        String token = obtainAuthToken("readUser3", "Pass1abc");

        // Create a book and capture its server-generated ID
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Specific Book",
                "author", "Specific Author",
                "genre", "Mystery",
                "pageCount", 320
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id"); // Extract the generated UUID from the response

        // GET /books/{id} — verify all fields match what we created
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/" + bookId)
        .then()
            .statusCode(200)
            .body("id", equalTo(bookId))
            .body("title", equalTo("Specific Book"))
            .body("author", equalTo("Specific Author"))
            .body("genre", equalTo("Mystery"))
            .body("pageCount", equalTo(320));
    }

    /**
     * GET /books/{id} with a UUID that doesn't correspond to any book
     * should return 404 Not Found.
     */
    @Test
    void getBookById_withNonExistentId_returns404() {
        String token = obtainAuthToken("readUser4", "Pass1abc");

        // Random UUID that was never used to create a book
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    /**
     * Users should only be able to see their OWN books.
     * If User A creates a book, User B requesting that book by ID should get 404
     * (not 403) to avoid leaking the existence of other users' books.
     */
    @Test
    void getBookById_ownedByDifferentUser_returns404() {
        String tokenA = obtainAuthToken("readUser5", "Pass1abc");
        String tokenB = obtainAuthToken("readUser6", "Pass1abc");

        // User A creates a book
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + tokenA)
            .body(Map.of(
                "title", "User A Book",
                "author", "User A Author",
                "genre", "Drama",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // User B tries to access User A's book — should get 404 (not 403)
        given()
            .header("Authorization", "Bearer " + tokenB)
        .when()
            .get("/books/" + bookId)
        .then()
            .statusCode(404);
    }

    /**
     * GET /books/{id} where {id} is not a valid UUID format should return 400.
     * The controller validates the path variable format before querying the DB.
     */
    @Test
    void getBookById_withInvalidUuid_returns400() {
        String token = obtainAuthToken("readUser7", "Pass1abc");

        // "not-a-uuid" is not valid UUID format — should fail fast with 400
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/not-a-uuid")
        .then()
            .statusCode(400)
            .body(equalTo("Book identifier is invalid"));
    }

    /**
     * GET /books without any auth header should be blocked by the interceptor.
     */
    @Test
    void getBooks_withoutAuth_returns401() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/books")
        .then()
            .statusCode(401);
    }

    // ==================== Book Update Tests (Requirements 8.1–8.5) ====================

    /**
     * Happy path: PUT /books/{id} with valid updated data should return 200
     * and the response body should reflect ALL the new values.
     */
    @Test
    void updateBook_withValidData_returns200WithUpdatedBook() {
        String token = obtainAuthToken("updateUser1", "Pass1abc");

        // Create the book we'll later update
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Original Title",
                "author", "Original Author",
                "genre", "Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // PUT /books/{id} with completely new values
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Updated Title",
                "author", "Updated Author",
                "genre", "Non-Fiction",
                "pageCount", 350
            ))
        .when()
            .put("/books/" + bookId)
        .then()
            .statusCode(200)                              // 200 OK = update succeeded
            .body("id", equalTo(bookId))                  // ID stays the same
            .body("title", equalTo("Updated Title"))      // Fields reflect new values
            .body("author", equalTo("Updated Author"))
            .body("genre", equalTo("Non-Fiction"))
            .body("pageCount", equalTo(350));
    }

    /**
     * PUT /books/{id} where {id} doesn't exist should return 404.
     * You can't update a book that was never created.
     */
    @Test
    void updateBook_withNonExistentId_returns404() {
        String token = obtainAuthToken("updateUser2", "Pass1abc");

        // Random UUID — no book exists with this ID
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Some Title",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 100
            ))
        .when()
            .put("/books/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    /**
     * PUT without auth should be blocked by the interceptor.
     */
    @Test
    void updateBook_withoutAuth_returns401() {
        given()
            .contentType(ContentType.JSON)
            .body(Map.of(
                "title", "Some Title",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 100
            ))
        .when()
            .put("/books/" + UUID.randomUUID())
        .then()
            .statusCode(401);
    }

    /**
     * Validation on update: blank title and out-of-range pageCount should both
     * return 400. Tests two invalid scenarios against the same book.
     */
    @Test
    void updateBook_withInvalidData_returns400() {
        String token = obtainAuthToken("updateUser3", "Pass1abc");

        // Create a valid book first
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Valid Title",
                "author", "Valid Author",
                "genre", "Fiction",
                "pageCount", 150
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // Attempt update with blank title — should fail validation
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "",
                "author", "Valid Author",
                "genre", "Fiction",
                "pageCount", 150
            ))
        .when()
            .put("/books/" + bookId)
        .then()
            .statusCode(400);

        // Attempt update with pageCount > 99999 — should fail validation
        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Valid Title",
                "author", "Valid Author",
                "genre", "Fiction",
                "pageCount", 100000
            ))
        .when()
            .put("/books/" + bookId)
        .then()
            .statusCode(400);
    }

    /**
     * PUT /books/{id} where {id} is not a valid UUID string should return 400
     * with a descriptive error message, without hitting the database.
     */
    @Test
    void updateBook_withInvalidUuid_returns400() {
        String token = obtainAuthToken("updateUser4", "Pass1abc");

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Some Title",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 100
            ))
        .when()
            .put("/books/not-a-uuid")
        .then()
            .statusCode(400)
            .body(equalTo("Book identifier is invalid"));
    }

    // ==================== Book Delete Tests (Requirements 9.1–9.5) ====================

    /**
     * Happy path: DELETE /books/{id} for an existing book returns 204 No Content.
     * 204 means "success, but there's nothing to send back in the body."
     */
    @Test
    void deleteBook_withValidId_returns204() {
        String token = obtainAuthToken("deleteUser1", "Pass1abc");

        // Create a book to delete
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Book To Delete",
                "author", "Delete Author",
                "genre", "Fiction",
                "pageCount", 150
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // DELETE /books/{id} — should succeed with 204
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + bookId)
        .then()
            .statusCode(204); // No Content — book is gone
    }

    /**
     * After deleting a book, attempting to GET it by ID should return 404.
     * This confirms the delete actually removed the record from the database.
     */
    @Test
    void deleteBook_thenGet_returns404() {
        String token = obtainAuthToken("deleteUser2", "Pass1abc");

        // Create a book
        String bookId = given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "Book To Delete Then Get",
                "author", "Delete Author",
                "genre", "Non-Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(201)
            .extract()
            .path("id");

        // Delete the book
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + bookId)
        .then()
            .statusCode(204);

        // Now try to GET the same book — should be 404 since it's deleted
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/" + bookId)
        .then()
            .statusCode(404);
    }

    /**
     * DELETE /books/{id} where {id} doesn't exist should return 404.
     * Can't delete something that isn't there.
     */
    @Test
    void deleteBook_withNonExistentId_returns404() {
        String token = obtainAuthToken("deleteUser3", "Pass1abc");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    /**
     * DELETE without auth should be blocked by the interceptor.
     */
    @Test
    void deleteBook_withoutAuth_returns401() {
        given()
        .when()
            .delete("/books/" + UUID.randomUUID())
        .then()
            .statusCode(401);
    }

    /**
     * DELETE /books/{id} where {id} is not a valid UUID format should return 400
     * with a descriptive message.
     */
    @Test
    void deleteBook_withInvalidUuid_returns400() {
        String token = obtainAuthToken("deleteUser5", "Pass1abc");

        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/not-a-uuid")
        .then()
            .statusCode(400)
            .body(equalTo("Book identifier is invalid"));
    }
}
