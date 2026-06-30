# Requirements Document

## Introduction

This document defines the requirements for integrating REST Assured into the Summer Reading Tracker project. REST Assured will serve as the API integration testing framework, enabling full-stack tests that exercise the Spring Boot application context — including HTTP routing, authentication (JWT), request/response serialization, and database interactions — using the H2 in-memory database.

## Glossary

- **REST_Assured_Framework**: The REST Assured library integrated into the Gradle build as a test dependency, providing a fluent DSL for HTTP-based API testing.
- **Integration_Test**: A test that boots the full Spring application context on a random port and sends real HTTP requests against the running server.
- **Test_Application_Context**: The Spring Boot application running with `@SpringBootTest(webEnvironment = RANDOM_PORT)` using H2 in-memory database and test-specific configuration.
- **Auth_Token**: A valid JWT obtained by registering and logging in a test user, used in the Authorization header for protected endpoints.
- **Base_Test_Class**: An abstract superclass that configures REST Assured's base URI/port and provides shared helper methods for all integration tests.

## Requirements

### Requirement 1: REST Assured Dependency Configuration

**User Story:** As a developer, I want REST Assured added to the project's test dependencies, so that I can write fluent API integration tests.

#### Acceptance Criteria

1. THE REST_Assured_Framework SHALL be declared as a `testImplementation` dependency in `build.gradle.kts` using a pinned GA (General Availability) release version that supports the Jakarta Servlet namespace required by Spring Boot 4.1.0.
2. WHEN the Gradle build executes the `dependencies` task, THE REST_Assured_Framework SHALL resolve all transitive dependencies successfully such that `./gradlew build` completes with exit code 0 and produces no dependency resolution errors.
3. THE REST_Assured_Framework SHALL include the `rest-assured-spring-mock-mvc` module as a `testImplementation` dependency to enable integration testing through Spring MockMvc without starting a full HTTP server.
4. WHEN a test class imports REST Assured's Spring MockMvc support and executes a request against a controller endpoint, THE REST_Assured_Framework SHALL enable the test to compile and execute successfully within the existing JUnit 5 and Spring Boot Test configuration.

### Requirement 2: Test Application Context Configuration

**User Story:** As a developer, I want a test configuration that boots the full application with H2, so that integration tests run against a realistic but isolated environment.

#### Acceptance Criteria

1. WHEN an integration test class annotated with `@SpringBootTest(webEnvironment = RANDOM_PORT)` starts, THE Test_Application_Context SHALL launch successfully on a random available port and make the port number injectable via `@LocalServerPort`.
2. THE Test_Application_Context SHALL use a `test.properties` file located in `src/test/resources` that overrides the production datasource to an H2 in-memory database with `create-drop` DDL strategy, an H2 JDBC driver, and the H2 dialect.
3. WHEN the Test_Application_Context starts, THE Test_Application_Context SHALL load all production-defined Spring beans such that controllers, services, repositories, the AuthInterceptor, and JwtUtility are injectable without error.
4. WHEN the test suite completes, THE Test_Application_Context SHALL drop all in-memory database tables via the `create-drop` strategy, ensuring no test data persists between test class executions.
5. THE Test_Application_Context SHALL NOT read from or write to the production SQLite database file (`todo.db`) during any integration test execution.

### Requirement 3: Base Test Infrastructure

**User Story:** As a developer, I want a reusable base test class, so that each test file does not repeat REST Assured setup boilerplate.

#### Acceptance Criteria

1. THE Base_Test_Class SHALL be annotated with `@SpringBootTest(webEnvironment = RANDOM_PORT)` and inject the assigned port via `@LocalServerPort`, configuring REST Assured's `baseURI` to `http://localhost` and `port` to the injected value in a `@BeforeEach` or `@BeforeAll` setup method.
2. THE Base_Test_Class SHALL provide a helper method `obtainAuthToken(String username, String password)` that sends a POST to `/register` followed by a POST to `/login` and returns the JWT string from the login response body.
3. THE Base_Test_Class SHALL call `RestAssured.reset()` in an `@AfterAll` method to restore default configuration after all tests in the class complete.
4. WHEN a subclass extends the Base_Test_Class, THE subclass SHALL inherit the configured REST Assured settings and helper methods without requiring additional annotations or setup code beyond its own `@Test` methods.

### Requirement 4: User Registration Endpoint Tests

**User Story:** As a developer, I want integration tests for the registration endpoint, so that I can verify user creation works end-to-end.

#### Acceptance Criteria

1. WHEN a POST request with a JSON body containing a username between 5 and 15 characters and a password between 5 and 15 characters that includes at least one uppercase letter, one lowercase letter, and one digit is sent to `/register`, THE Integration_Test SHALL verify the response status is 201 Created and the response body is empty.
2. WHEN a POST request is sent to `/register` with a username that already exists in the database, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the username must be unique.
3. WHEN a POST request with a JSON body where the username field is null or the password field is null is sent to `/register`, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the empty field.
4. WHEN a POST request is sent to `/register` with a password that does not contain at least one uppercase letter, one lowercase letter, and one digit, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the password character requirements.
5. WHEN a POST request is sent to `/register` with a username or password shorter than 5 characters or longer than 15 characters, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the length requirement.

### Requirement 5: User Login Endpoint Tests

**User Story:** As a developer, I want integration tests for the login endpoint, so that I can verify authentication and token generation end-to-end.

#### Acceptance Criteria

1. WHEN a POST request with a JSON body containing `username` and `password` fields of a previously registered user is sent to `/login`, THE Integration_Test SHALL verify the response status is 200 OK and the body contains a non-empty string with three dot-separated Base64URL segments (JWT compact serialization format).
2. WHEN a POST request with a valid username but incorrect password is sent to `/login`, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a non-empty error message string.
3. WHEN a POST request with a username that does not exist in the database is sent to `/login`, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a non-empty error message string.
4. WHEN a POST request with a null or missing username or password field is sent to `/login`, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a non-empty error message string.
5. THE Integration_Test SHALL register a test user via the `/register` endpoint (or equivalent test setup) before executing any login test, to ensure valid credentials exist in the database.

### Requirement 6: Book CRUD Endpoint Tests — Create

**User Story:** As a developer, I want integration tests for book creation, so that I can verify the POST /books flow end-to-end with authentication.

#### Acceptance Criteria

1. WHEN an authenticated POST request containing a JSON body with a non-blank title (max 255 characters), a non-blank author (max 255 characters), an optional genre (max 100 characters), and a pageCount between 1 and 25000 is sent to `/books`, THE Integration_Test SHALL verify the response status is 201 Created and the response body contains the created book with a system-generated UUID `id` and the submitted title, author, genre, and pageCount values.
2. WHEN a POST request with no Authorization header is sent to `/books`, THE Integration_Test SHALL verify the response status is 401 Unauthorized.
3. WHEN a POST request with an invalid or expired Bearer token in the Authorization header is sent to `/books`, THE Integration_Test SHALL verify the response status is 401 Unauthorized.
4. WHEN an authenticated POST request with a blank or missing title is sent to `/books`, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the validation failure.
5. WHEN an authenticated POST request with a pageCount less than 1 or greater than 25000 is sent to `/books`, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the validation failure.

### Requirement 7: Book CRUD Endpoint Tests — Read

**User Story:** As a developer, I want integration tests for book retrieval, so that I can verify GET /books and GET /books/{id} work end-to-end.

#### Acceptance Criteria

1. WHEN an authenticated GET request is sent to `/books`, THE Integration_Test SHALL verify the response status is 200 OK and the body contains a JSON array where each element includes the fields `id`, `title`, `author`, `genre`, and `pageCount`, and only books belonging to the authenticated user are present in the array.
2. WHEN an authenticated GET request is sent to `/books` and the user has no books, THE Integration_Test SHALL verify the response status is 200 OK and the body contains an empty JSON array.
3. WHEN an authenticated GET request is sent to `/books/{id}` with a valid book ID owned by the authenticated user, THE Integration_Test SHALL verify the response status is 200 OK and the body contains a JSON object with the fields `id`, `title`, `author`, `genre`, and `pageCount` matching the persisted book data.
4. WHEN an authenticated GET request is sent to `/books/{id}` with a non-existent ID, THE Integration_Test SHALL verify the response status is 404 Not Found.
5. WHEN an authenticated GET request is sent to `/books/{id}` with an ID belonging to a different user, THE Integration_Test SHALL verify the response status is 404 Not Found.
6. IF the `{id}` path variable is not a valid UUID, THEN THE Integration_Test SHALL verify the response status is 400 Bad Request.
7. WHEN an unauthenticated GET request (no Authorization header) is sent to `/books`, THE Integration_Test SHALL verify the response status is 401 Unauthorized.

### Requirement 8: Book CRUD Endpoint Tests — Update

**User Story:** As a developer, I want integration tests for book updates, so that I can verify PUT /books/{id} works end-to-end.

#### Acceptance Criteria

1. WHEN an authenticated PUT request with a JSON body containing updated title, author, genre, and pageCount values (all passing validation: title and author non-blank and at most 255 characters, genre at most 100 characters, pageCount between 1 and 99999) is sent to `/books/{id}` for an existing book, THE Integration_Test SHALL verify the response status is 200 OK and the response body contains the updated title, author, genre, and pageCount values matching the request.
2. WHEN an authenticated PUT request is sent to `/books/{id}` with a UUID that does not correspond to any stored book for the authenticated user, THE Integration_Test SHALL verify the response status is 404 Not Found.
3. WHEN a PUT request is sent to `/books/{id}` without an Authorization header, THE Integration_Test SHALL verify the response status is 401 Unauthorized.
4. WHEN an authenticated PUT request with invalid book data (empty title, empty author, or pageCount outside 1–99999) is sent to `/books/{id}` for an existing book, THE Integration_Test SHALL verify the response status is 400 Bad Request and the response body contains an error message indicating the validation failure.
5. WHEN an authenticated PUT request is sent to `/books/{id}` where `{id}` is not a valid UUID format, THE Integration_Test SHALL verify the response status is 400 Bad Request.

### Requirement 9: Book CRUD Endpoint Tests — Delete

**User Story:** As a developer, I want integration tests for book deletion, so that I can verify DELETE /books/{id} works end-to-end.

#### Acceptance Criteria

1. WHEN an authenticated DELETE request is sent to `/books/{id}` with a valid book ID owned by the authenticated user, THE Integration_Test SHALL verify the response status is 204 No Content.
2. WHEN a subsequent authenticated GET request is sent to `/books/{id}` for the deleted book, THE Integration_Test SHALL verify the response status is 404 Not Found, confirming the book was actually removed from the database.
3. WHEN an authenticated DELETE request is sent to `/books/{id}` with a UUID that does not correspond to any stored book for the authenticated user, THE Integration_Test SHALL verify the response status is 404 Not Found.
4. WHEN a DELETE request is sent to `/books/{id}` without an Authorization header, THE Integration_Test SHALL verify the response status is 401 Unauthorized.
5. WHEN an authenticated DELETE request is sent to `/books/{id}` where `{id}` is not a valid UUID format, THE Integration_Test SHALL verify the response status is 400 Bad Request.

### Requirement 10: Authentication Enforcement Tests

**User Story:** As a developer, I want integration tests that verify the AuthInterceptor rejects invalid tokens, so that I can confirm security is enforced end-to-end.

#### Acceptance Criteria

1. WHEN a request with an expired JWT is sent to any endpoint not excluded by WebConfig (endpoints other than /register and /login), THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a message indicating the token is invalid or expired.
2. WHEN a request with an Authorization header that does not start with "Bearer " is sent to any endpoint not excluded by WebConfig, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a message indicating the header is missing or malformed.
3. WHEN a request with no Authorization header is sent to any endpoint not excluded by WebConfig, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a message indicating the header is missing or malformed.
4. WHEN a request with a JWT signed by a different secret key is sent to any endpoint not excluded by WebConfig, THE Integration_Test SHALL verify the response status is 401 Unauthorized and the response body contains a message indicating the token is invalid or expired.
5. WHEN a request with a valid, non-expired JWT is sent to any endpoint not excluded by WebConfig, THE Integration_Test SHALL verify the response status is not 401 Unauthorized, confirming that authenticated requests are allowed through.
