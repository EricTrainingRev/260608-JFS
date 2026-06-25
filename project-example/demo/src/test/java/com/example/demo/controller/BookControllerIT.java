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
 * Integration tests for BookController endpoints.
 * Extends BaseIntegrationTest for REST Assured configuration and auth helpers.
 */
class BookControllerIT extends BaseIntegrationTest {

    // ==================== Book Creation Tests (Requirements 6.1–6.5) ====================

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
            .statusCode(201)
            .body("id", notNullValue())
            .body("title", equalTo("The Great Gatsby"))
            .body("author", equalTo("F. Scott Fitzgerald"))
            .body("genre", equalTo("Fiction"))
            .body("pageCount", equalTo(180));
    }

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
            .statusCode(401);
    }

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
            .statusCode(401);
    }

    @Test
    void createBook_withBlankTitle_returns400() {
        String token = obtainAuthToken("bookUser2", "Pass1abc");

        given()
            .contentType(ContentType.JSON)
            .header("Authorization", "Bearer " + token)
            .body(Map.of(
                "title", "",
                "author", "Some Author",
                "genre", "Fiction",
                "pageCount", 200
            ))
        .when()
            .post("/books")
        .then()
            .statusCode(400);
    }

    @Test
    void createBook_withInvalidPageCount_returns400() {
        String token = obtainAuthToken("bookUser3", "Pass1abc");

        // pageCount < 1
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

        // pageCount > 25000
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

        // GET /books should return array of size 2 with only this user's books
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(200)
            .body("$", hasSize(2))
            .body("[0].id", notNullValue())
            .body("[0].title", notNullValue())
            .body("[0].author", notNullValue())
            .body("[0].pageCount", notNullValue());
    }

    @Test
    void getBooks_withNoBooks_returns200WithEmptyArray() {
        String token = obtainAuthToken("readUser2", "Pass1abc");

        // Fresh user with no books — GET /books should return empty array
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books")
        .then()
            .statusCode(200)
            .body("$", empty());
    }

    @Test
    void getBookById_withValidId_returns200WithBook() {
        String token = obtainAuthToken("readUser3", "Pass1abc");

        // Create a book and extract the ID
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
            .path("id");

        // GET /books/{id} should return the book with matching fields
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

    @Test
    void getBookById_withNonExistentId_returns404() {
        String token = obtainAuthToken("readUser4", "Pass1abc");

        // GET /books/{random-uuid} should return 404
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    @Test
    void getBookById_ownedByDifferentUser_returns404() {
        String tokenA = obtainAuthToken("readUser5", "Pass1abc");
        String tokenB = obtainAuthToken("readUser6", "Pass1abc");

        // Create a book as user A
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

        // GET as user B should return 404
        given()
            .header("Authorization", "Bearer " + tokenB)
        .when()
            .get("/books/" + bookId)
        .then()
            .statusCode(404);
    }

    @Test
    void getBookById_withInvalidUuid_returns400() {
        String token = obtainAuthToken("readUser7", "Pass1abc");

        // GET /books/not-a-uuid should return 400
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/not-a-uuid")
        .then()
            .statusCode(400)
            .body(equalTo("Book identifier is invalid"));
    }

    @Test
    void getBooks_withoutAuth_returns401() {
        // GET /books without Authorization header should return 401
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/books")
        .then()
            .statusCode(401);
    }

    // ==================== Book Update Tests (Requirements 8.1–8.5) ====================

    @Test
    void updateBook_withValidData_returns200WithUpdatedBook() {
        String token = obtainAuthToken("updateUser1", "Pass1abc");

        // Create a book first
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

        // PUT /books/{id} with updated values
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
            .statusCode(200)
            .body("id", equalTo(bookId))
            .body("title", equalTo("Updated Title"))
            .body("author", equalTo("Updated Author"))
            .body("genre", equalTo("Non-Fiction"))
            .body("pageCount", equalTo(350));
    }

    @Test
    void updateBook_withNonExistentId_returns404() {
        String token = obtainAuthToken("updateUser2", "Pass1abc");

        // PUT /books/{random-uuid} should return 404
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

    @Test
    void updateBook_withoutAuth_returns401() {
        // PUT without Authorization header should return 401
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

    @Test
    void updateBook_withInvalidData_returns400() {
        String token = obtainAuthToken("updateUser3", "Pass1abc");

        // Create a book first
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

        // PUT with blank title should return 400
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

        // PUT with invalid pageCount (> 99999) should return 400
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

    @Test
    void updateBook_withInvalidUuid_returns400() {
        String token = obtainAuthToken("updateUser4", "Pass1abc");

        // PUT /books/not-a-uuid should return 400
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

    @Test
    void deleteBook_withValidId_returns204() {
        String token = obtainAuthToken("deleteUser1", "Pass1abc");

        // Create a book first
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

        // DELETE /books/{id} should return 204 No Content
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + bookId)
        .then()
            .statusCode(204);
    }

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

        // DELETE the book
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + bookId)
        .then()
            .statusCode(204);

        // GET the same book should return 404
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/books/" + bookId)
        .then()
            .statusCode(404);
    }

    @Test
    void deleteBook_withNonExistentId_returns404() {
        String token = obtainAuthToken("deleteUser3", "Pass1abc");

        // DELETE /books/{random-uuid} should return 404
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/" + UUID.randomUUID())
        .then()
            .statusCode(404);
    }

    @Test
    void deleteBook_withoutAuth_returns401() {
        // DELETE without Authorization header should return 401
        given()
        .when()
            .delete("/books/" + UUID.randomUUID())
        .then()
            .statusCode(401);
    }

    @Test
    void deleteBook_withInvalidUuid_returns400() {
        String token = obtainAuthToken("deleteUser5", "Pass1abc");

        // DELETE /books/not-a-uuid should return 400
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .delete("/books/not-a-uuid")
        .then()
            .statusCode(400)
            .body(equalTo("Book identifier is invalid"));
    }
}
