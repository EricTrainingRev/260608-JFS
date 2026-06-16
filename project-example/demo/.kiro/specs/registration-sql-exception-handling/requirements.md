# Requirements Document

## Introduction

This feature adds SQL exception handling to the existing user registration flow. Currently, the `UserController` only handles `RegistrationFailure` exceptions (business logic validation). If the database layer throws an SQL-related exception during `userRepo.save(user)` — such as a connection failure, constraint violation, or query timeout — the application returns a raw 500 error with implementation details exposed. This feature introduces structured handling for those database-level exceptions so that YOUR MAJESTY's users get a clean, informative response and sensitive internals stay hidden.

## Glossary

- **Registration_Service**: The `UserService` class responsible for validating and persisting new user registrations
- **User_Controller**: The `UserController` REST controller that exposes the `/register` endpoint and assembles HTTP responses
- **User_Repository**: The `UserRepo` interface backed by Spring Data JPA that performs database operations on the `users` table
- **SQL_Exception**: Any exception originating from the database/JDBC layer during a persistence operation, including `DataAccessException` and its subclasses provided by Spring
- **Constraint_Violation**: A database-level violation of a unique or not-null constraint (e.g., duplicate username reaching the DB despite service-level checks)
- **Connection_Failure**: An exception indicating the application cannot communicate with the SQLite database file
- **Query_Timeout**: An exception indicating a database operation exceeded its allowed execution time

## Requirements

### Requirement 1: Handle Database Constraint Violations

**User Story:** As a user, I want to receive a clear error message when a database constraint is violated during registration, so that I understand what went wrong without seeing raw SQL errors.

#### Acceptance Criteria

1. WHEN a `DataIntegrityViolationException` is thrown during user registration, THE User_Controller SHALL return an HTTP 409 Conflict response with a plain-text body containing a message indicating the registration could not be completed due to a data conflict
2. WHEN a `DataIntegrityViolationException` is thrown during user registration, THE User_Controller SHALL NOT expose internal database details, table names, column names, or SQL statements in the response body
3. WHEN a `DataIntegrityViolationException` is thrown during user registration, THE Registration_Service SHALL log the exception message and stack trace at ERROR level for developer diagnostics
4. WHEN a `DataIntegrityViolationException` is thrown during user registration, THE User_Controller exception handler SHALL be scoped to the User_Controller only, consistent with the existing `RegistrationFailure` handler pattern

### Requirement 2: Handle Database Connection Failures

**User Story:** As a user, I want to receive a meaningful error message when the database is unreachable during registration, so that I know the issue is temporary and not caused by my input.

#### Acceptance Criteria

1. WHEN a `DataAccessResourceFailureException` is thrown during user registration, THE User_Controller SHALL return an HTTP 503 Service Unavailable response with a plain-text body containing a message indicating the service is temporarily unavailable and the user may retry
2. WHEN a `DataAccessResourceFailureException` is thrown during user registration, THE User_Controller SHALL NOT expose stack traces, Java class names, file paths, connection strings, or internal infrastructure details in the response body
3. WHEN a `DataAccessResourceFailureException` is thrown during user registration, THE Registration_Service SHALL log the full exception message and stack trace at ERROR level for developer diagnostics
4. IF a `DataAccessResourceFailureException` is thrown during user registration, THEN THE system SHALL NOT persist a partial or incomplete User record in the database

### Requirement 3: Handle Query Timeout Exceptions

**User Story:** As a user, I want to receive a clear response when a database operation times out during registration, so that I know to try again later.

#### Acceptance Criteria

1. WHEN a `QueryTimeoutException` is thrown during user registration, THE User_Controller SHALL return an HTTP 503 Service Unavailable response with a plain-text string body containing a message indicating the request timed out and to try again later
2. WHEN a `QueryTimeoutException` is thrown during user registration, THE User_Controller SHALL NOT include SQL statements, timeout duration values, stack traces, or Spring internal class names in the response body
3. WHEN a `QueryTimeoutException` is thrown during user registration, THE Registration_Service SHALL log the exception class name, exception message, and stack trace at ERROR level
4. WHEN a `QueryTimeoutException` is thrown during user registration, THE User_Controller SHALL NOT persist the user entity to the database

### Requirement 4: Handle Generic Data Access Exceptions

**User Story:** As a user, I want all other unexpected database errors during registration to return a safe, generic error message, so that no internal details leak through unhandled paths.

#### Acceptance Criteria

1. WHEN a `DataAccessException` that is not a `DataIntegrityViolationException`, `DataAccessResourceFailureException`, or `QueryTimeoutException` is thrown during user registration, THE User_Controller SHALL return an HTTP 500 Internal Server Error response with a message indicating an unexpected error occurred
2. WHEN an uncategorized `DataAccessException` is thrown, THE User_Controller SHALL NOT expose exception class names, stack traces, SQL details, or internal configuration values in the response body
3. WHEN an uncategorized `DataAccessException` is thrown, THE Registration_Service SHALL log the full exception message and stack trace at ERROR level for developer diagnostics
4. WHEN a `DataAccessException` subclass covered by Requirements 1-3 is thrown during user registration, THE User_Controller SHALL delegate to the corresponding specific handler and SHALL NOT trigger the generic catch-all handler

### Requirement 5: Preserve Existing Business Logic Exception Handling

**User Story:** As a developer, I want the new SQL exception handling to coexist with the existing `RegistrationFailure` handling, so that business validation errors continue to return HTTP 400 as before.

#### Acceptance Criteria

1. WHEN a `RegistrationFailure` exception is thrown during user registration, THE User_Controller SHALL return an HTTP 400 Bad Request response with the response body containing the exception's message string
2. WHEN a `RegistrationFailure` exception is thrown during user registration, THE User_Controller SHALL handle it using the `RegistrationFailure`-specific exception handler and SHALL NOT route it to any `DataAccessException` handler
3. WHEN a `DataAccessException` is thrown during user registration, THE User_Controller SHALL handle it using the SQL exception handlers defined in Requirements 1-4 and SHALL NOT affect the response status or body of any `RegistrationFailure` handling
