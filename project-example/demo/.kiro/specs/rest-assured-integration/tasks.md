# Implementation Plan: REST Assured Integration

## Overview

This plan implements REST Assured integration tests for the Summer Reading Tracker API. The work progresses from build configuration, through shared test infrastructure, to endpoint-specific integration test classes — each building on the previous step. All tests use `@SpringBootTest(webEnvironment = RANDOM_PORT)` with H2 and REST Assured's BDD-style DSL.

## Tasks

- [ ] 1. Add REST Assured dependencies and test configuration
  - [ ] 1.1 Add REST Assured test dependencies to build.gradle.kts
    - Add `testImplementation("io.rest-assured:rest-assured:5.5.1")` to the dependencies block
    - Add `testImplementation("io.rest-assured:spring-mock-mvc:5.5.1")` to the dependencies block
    - Verify the build compiles successfully with `./gradlew build`
    - _Requirements: 1.1, 1.2, 1.3, 1.4_

  - [ ] 1.2 Create test.properties for H2 datasource configuration
    - Create `src/test/resources/test.properties`
    - Configure H2 in-memory datasource: `spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1`
    - Set driver class: `spring.datasource.driver-class-name=org.h2.Driver`
    - Set dialect: `spring.jpa.database-platform=org.hibernate.dialect.H2Dialect`
    - Set DDL auto: `spring.jpa.hibernate.ddl-auto=create-drop`
    - Enable SQL logging: `spring.jpa.show-sql=true` and `spring.jpa.properties.hibernate.format_sql=true`
    - _Requirements: 2.2, 2.4, 2.5_

- [ ] 2. Implement base test infrastructure
  - [ ] 2.1 Create BaseIntegrationTest abstract class
    - Create `src/test/java/com/example/demo/controller/BaseIntegrationTest.java`
    - Annotate with `@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)`
    - Annotate with `@TestPropertySource(locations = "classpath:test.properties")`
    - Inject `@LocalServerPort` into a protected `port` field
    - Implement `@BeforeEach` method that sets `RestAssured.baseURI = "http://localhost"` and `RestAssured.port = port`
    - Implement `@AfterAll` static method that calls `RestAssured.reset()`
    - Implement `obtainAuthToken(String username, String password)` helper that POSTs to `/register` then `/login` and returns the JWT string
    - _Requirements: 2.1, 2.3, 3.1, 3.2, 3.3, 3.4_

- [ ] 3. Checkpoint - Verify base infrastructure
  - Ensure the project compiles with `./gradlew build`, ask the user if questions arise.

- [ ] 4. Implement User endpoint integration tests
  - [ ] 4.1 Create UserControllerIT — registration tests
    - Create `src/test/java/com/example/demo/controller/UserControllerIT.java` extending `BaseIntegrationTest`
    - Implement `register_withValidCredentials_returns201()` — POST valid username (5-15 chars) and password (5-15 chars with uppercase, lowercase, digit), assert 201
    - Implement `register_withDuplicateUsername_returns400()` — register same username twice, assert 400 with error message about uniqueness
    - Implement `register_withNullUsername_returns400()` — POST with null username, assert 400 with error message
    - Implement `register_withNullPassword_returns400()` — POST with null password, assert 400 with error message
    - Implement `register_withInvalidPasswordCharacters_returns400()` — POST password missing required character types, assert 400
    - Implement `register_withCredentialsTooShort_returns400()` — POST username or password under 5 chars, assert 400
    - Implement `register_withCredentialsTooLong_returns400()` — POST username or password over 15 chars, assert 400
    - Use unique usernames per test method to avoid collisions
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5_

  - [ ] 4.2 Create UserControllerIT — login tests
    - Add login test methods to `UserControllerIT`
    - Implement `login_withValidCredentials_returns200WithJwt()` — register user, login, assert 200 and JWT format (three dot-separated segments)
    - Implement `login_withWrongPassword_returns401()` — register user, login with wrong password, assert 401
    - Implement `login_withNonExistentUser_returns401()` — login with unregistered username, assert 401
    - Implement `login_withNullFields_returns401()` — login with null username/password, assert 401
    - Each login test registers its own user first via `/register`
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5_

- [ ] 5. Checkpoint - Verify user endpoint tests pass
  - Ensure all tests pass with `./gradlew test`, ask the user if questions arise.

- [ ] 6. Implement Book CRUD integration tests — Create and Read
  - [ ] 6.1 Create BookControllerIT — book creation tests
    - Create `src/test/java/com/example/demo/controller/BookControllerIT.java` extending `BaseIntegrationTest`
    - Implement `createBook_withValidData_returns201WithBook()` — authenticated POST with valid title, author, genre, pageCount; assert 201 and response body has UUID id plus submitted fields
    - Implement `createBook_withoutAuth_returns401()` — POST without Authorization header, assert 401
    - Implement `createBook_withInvalidToken_returns401()` — POST with garbage Bearer token, assert 401
    - Implement `createBook_withBlankTitle_returns400()` — authenticated POST with blank title, assert 400
    - Implement `createBook_withInvalidPageCount_returns400()` — authenticated POST with pageCount < 1 or > 25000, assert 400
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5_

  - [ ] 6.2 Add BookControllerIT — book read tests
    - Implement `getBooks_withBooks_returns200WithArray()` — create books, GET `/books`, assert 200 with array containing only authenticated user's books
    - Implement `getBooks_withNoBooks_returns200WithEmptyArray()` — fresh user, GET `/books`, assert 200 with empty array
    - Implement `getBookById_withValidId_returns200WithBook()` — create book, GET `/books/{id}`, assert 200 with matching fields
    - Implement `getBookById_withNonExistentId_returns404()` — GET `/books/{random-uuid}`, assert 404
    - Implement `getBookById_ownedByDifferentUser_returns404()` — create book as user A, GET as user B, assert 404
    - Implement `getBookById_withInvalidUuid_returns400()` — GET `/books/not-a-uuid`, assert 400
    - Implement `getBooks_withoutAuth_returns401()` — GET `/books` without Authorization, assert 401
    - _Requirements: 7.1, 7.2, 7.3, 7.4, 7.5, 7.6, 7.7_

- [ ] 7. Implement Book CRUD integration tests — Update and Delete
  - [ ] 7.1 Add BookControllerIT — book update tests
    - Implement `updateBook_withValidData_returns200WithUpdatedBook()` — create book, PUT with new values, assert 200 with updated fields
    - Implement `updateBook_withNonExistentId_returns404()` — PUT to `/books/{random-uuid}`, assert 404
    - Implement `updateBook_withoutAuth_returns401()` — PUT without Authorization, assert 401
    - Implement `updateBook_withInvalidData_returns400()` — PUT with blank title or invalid pageCount, assert 400
    - Implement `updateBook_withInvalidUuid_returns400()` — PUT to `/books/not-a-uuid`, assert 400
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5_

  - [ ] 7.2 Add BookControllerIT — book delete tests
    - Implement `deleteBook_withValidId_returns204()` — create book, DELETE, assert 204
    - Implement `deleteBook_thenGet_returns404()` — delete book, GET same ID, assert 404
    - Implement `deleteBook_withNonExistentId_returns404()` — DELETE `/books/{random-uuid}`, assert 404
    - Implement `deleteBook_withoutAuth_returns401()` — DELETE without Authorization, assert 401
    - Implement `deleteBook_withInvalidUuid_returns400()` — DELETE `/books/not-a-uuid`, assert 400
    - _Requirements: 9.1, 9.2, 9.3, 9.4, 9.5_

- [ ] 8. Implement authentication enforcement integration tests
  - [ ] 8.1 Create AuthInterceptorIT
    - Create `src/test/java/com/example/demo/controller/AuthInterceptorIT.java` extending `BaseIntegrationTest`
    - Implement `request_withExpiredToken_returns401()` — craft an expired JWT (use JJWT to create one with past expiration), send to `/books`, assert 401
    - Implement `request_withMalformedAuthHeader_returns401()` — send Authorization header without "Bearer " prefix, assert 401
    - Implement `request_withNoAuthHeader_returns401()` — send request with no Authorization header to `/books`, assert 401
    - Implement `request_withWrongSecretToken_returns401()` — craft JWT signed with different secret, send to `/books`, assert 401
    - Implement `request_withValidToken_doesNotReturn401()` — obtain valid token via helper, send to `/books`, assert status is not 401
    - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.5_

- [ ] 9. Final checkpoint - Ensure all integration tests pass
  - Run `./gradlew test` and ensure all tests pass, ask the user if questions arise.

## Notes

- All tests use Java 21 and follow the project naming convention `{action}_{condition}_{expectedOutcome}()`
- Integration test classes use the `IT` suffix per project convention
- Tests mirror the main source structure under `src/test/java/com/example/demo/controller/`
- No property-based tests — the design explicitly states example-based integration tests are appropriate for this feature
- Each test references specific requirement acceptance criteria for traceability
- Checkpoints ensure incremental validation of the test infrastructure
- The `obtainAuthToken` helper handles user registration + login, so individual tests stay focused on their scenario

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "1.2"] },
    { "id": 1, "tasks": ["2.1"] },
    { "id": 2, "tasks": ["4.1", "4.2"] },
    { "id": 3, "tasks": ["6.1", "6.2"] },
    { "id": 4, "tasks": ["7.1", "7.2", "8.1"] }
  ]
}
```
