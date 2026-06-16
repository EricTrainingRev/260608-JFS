# Implementation Plan: Registration SQL Exception Handling

## Overview

Add four `@ExceptionHandler` methods to `UserController` to handle `DataIntegrityViolationException` (409), `DataAccessResourceFailureException` (503), `QueryTimeoutException` (503), and a catch-all `DataAccessException` (500). Each handler returns a static, safe plain-text message and logs the exception at ERROR level. No new classes are created; the existing `RegistrationFailure` → 400 handler remains unchanged.

## Tasks

- [ ] 1. Add SQL exception handler methods to UserController
  - [ ] 1.1 Add the `handleDataIntegrityViolation` exception handler method
    - Import `DataIntegrityViolationException` from `org.springframework.dao`
    - Add an `@ExceptionHandler(DataIntegrityViolationException.class)` method to `UserController`
    - The method should accept `DataIntegrityViolationException` as its parameter
    - Log the exception at ERROR level using SLF4J (use Lombok's `@Slf4j` annotation on the class)
    - Return `ResponseEntity<String>` with HTTP 409 Conflict and body: `"Could not complete registration: data conflict"`
    - _Requirements: 1.1, 1.2, 1.3, 1.4_

  - [ ] 1.2 Add the `handleResourceFailure` exception handler method
    - Import `DataAccessResourceFailureException` from `org.springframework.dao`
    - Add an `@ExceptionHandler(DataAccessResourceFailureException.class)` method to `UserController`
    - The method should accept `DataAccessResourceFailureException` as its parameter
    - Log the exception at ERROR level using SLF4J
    - Return `ResponseEntity<String>` with HTTP 503 Service Unavailable and body: `"Service temporarily unavailable, please try again later"`
    - _Requirements: 2.1, 2.2, 2.3, 2.4_

  - [ ] 1.3 Add the `handleQueryTimeout` exception handler method
    - Import `QueryTimeoutException` from `org.springframework.dao`
    - Add an `@ExceptionHandler(QueryTimeoutException.class)` method to `UserController`
    - The method should accept `QueryTimeoutException` as its parameter
    - Log the exception at ERROR level using SLF4J
    - Return `ResponseEntity<String>` with HTTP 503 Service Unavailable and body: `"Request timed out, please try again later"`
    - _Requirements: 3.1, 3.2, 3.3, 3.4_

  - [ ] 1.4 Add the `handleGenericDataAccess` catch-all exception handler method
    - Import `DataAccessException` from `org.springframework.dao`
    - Add an `@ExceptionHandler(DataAccessException.class)` method to `UserController`
    - The method should accept `DataAccessException` as its parameter
    - Log the exception at ERROR level using SLF4J
    - Return `ResponseEntity<String>` with HTTP 500 Internal Server Error and body: `"An unexpected error occurred during registration"`
    - _Requirements: 4.1, 4.2, 4.3, 4.4_

- [ ] 2. Checkpoint - Verify compilation
  - Ensure the project compiles cleanly with `./gradlew build`
  - Verify that the existing `handleRegistrationFailure` method is untouched
  - Ask the user if questions arise

- [ ] 3. Add test configuration for H2 in-memory database
  - [ ] 3.1 Create or verify `src/test/resources/test.properties` for H2 test database
    - Set `spring.datasource.url=jdbc:h2:mem:testdb`
    - Set `spring.datasource.driver-class-name=org.h2.Driver`
    - Set `spring.jpa.database-platform=org.hibernate.dialect.H2Dialect`
    - Set `spring.jpa.hibernate.ddl-auto=create-drop`
    - _Requirements: supports all test scenarios_

- [ ] 4. Write unit tests for exception handlers
  - [ ]* 4.1 Write unit tests for `handleDataIntegrityViolation`
    - Create `src/test/java/com/example/demo/controller/UserControllerExceptionHandlerTest.java`
    - Use `@WebMvcTest(UserController.class)` and mock `UserService`
    - Configure mock to throw `DataIntegrityViolationException` when `registerUser` is called
    - Assert response status is 409 and body is `"Could not complete registration: data conflict"`
    - Assert response body does NOT contain SQL keywords, class names, or stack traces
    - _Requirements: 1.1, 1.2_

  - [ ]* 4.2 Write unit tests for `handleResourceFailure`
    - Add test method to the same test class
    - Configure mock to throw `DataAccessResourceFailureException` when `registerUser` is called
    - Assert response status is 503 and body is `"Service temporarily unavailable, please try again later"`
    - Assert response body does NOT contain internal infrastructure details
    - _Requirements: 2.1, 2.2_

  - [ ]* 4.3 Write unit tests for `handleQueryTimeout`
    - Add test method to the same test class
    - Configure mock to throw `QueryTimeoutException` when `registerUser` is called
    - Assert response status is 503 and body is `"Request timed out, please try again later"`
    - Assert response body does NOT contain SQL statements or timeout values
    - _Requirements: 3.1, 3.2_

  - [ ]* 4.4 Write unit tests for `handleGenericDataAccess`
    - Add test method to the same test class
    - Configure mock to throw a generic `DataAccessException` subclass (e.g., `CannotAcquireLockException`) when `registerUser` is called
    - Assert response status is 500 and body is `"An unexpected error occurred during registration"`
    - Assert response body does NOT contain exception class names or stack traces
    - _Requirements: 4.1, 4.2_

  - [ ]* 4.5 Write unit test verifying `RegistrationFailure` still returns 400
    - Add test method to the same test class
    - Configure mock to throw `RegistrationFailure` with a custom message when `registerUser` is called
    - Assert response status is 400 and body equals the exception message
    - Assert the SQL exception handlers are NOT triggered
    - _Requirements: 5.1, 5.2, 5.3_

  - [ ]* 4.6 Write unit test verifying specific exceptions are NOT caught by generic handler
    - Configure mock to throw `DataIntegrityViolationException` (which IS a `DataAccessException` subclass)
    - Assert response is 409 (not 500), confirming Spring routes to the most specific handler
    - _Requirements: 4.4_

- [ ] 5. Final checkpoint - Ensure all tests pass
  - Run `./gradlew test` and ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- No new classes are needed — all changes go into `UserController.java`
- The `@Slf4j` Lombok annotation is added to the controller to enable logging (replaces manual Logger declaration)
- `UserService` is NOT modified — exceptions propagate naturally from `userRepo.save(user)`
- Spring resolves handler specificity automatically; the catch-all `DataAccessException` handler only fires for unhandled subclasses
- Each task references specific acceptance criteria sub-numbers (e.g., 1.1 = Requirement 1, Criterion 1)

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "1.2", "1.3", "1.4"] },
    { "id": 1, "tasks": ["3.1"] },
    { "id": 2, "tasks": ["4.1", "4.2", "4.3", "4.4", "4.5", "4.6"] }
  ]
}
```
