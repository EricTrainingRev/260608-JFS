# Requirements Document

## Introduction

This feature adds a Book entity and full CRUD management to the Summer Reading Tracker. Authenticated users can create, read, update, and delete books in their personal reading log. Each book captures a title, author, genre, and page count. Books are scoped to the owning user — a user can only manage their own books.

## Glossary

- **Book_Service**: The Spring service layer responsible for book business logic and validation
- **Book_Controller**: The REST controller exposing book management endpoints
- **Book_Repository**: The Spring Data JPA repository providing persistence operations for books
- **Book**: The JPA entity representing a book in a user's reading log, containing title, author, genre, and page count
- **Authenticated_User**: A user who has provided a valid JWT in the Authorization header, with their userId available as a request attribute

## Requirements

### Requirement 1: Create a Book

**User Story:** As an authenticated user, I want to add a book to my reading log, so that I can track what I have read this summer.

#### Acceptance Criteria

1. WHEN an Authenticated_User submits a book creation request containing a title, an author, and a page count that satisfies all validation criteria, THE Book_Controller SHALL respond with HTTP status 201 and the created Book including its generated UUID identifier
2. WHEN a book creation request is received, THE Book_Service SHALL associate the new Book with the Authenticated_User's identifier extracted from the JWT token
3. IF a book creation request is missing a title or the title is blank, THEN THE Book_Service SHALL reject the request with HTTP status 400 and an error message indicating the title is required
4. IF a book creation request is missing an author or the author is blank, THEN THE Book_Service SHALL reject the request with HTTP status 400 and an error message indicating the author is required
5. IF a book creation request contains a page count less than 1 or greater than 25000, THEN THE Book_Service SHALL reject the request with HTTP status 400 and an error message indicating the page count must be between 1 and 25000
6. IF a book creation request contains a title longer than 255 characters or an author longer than 255 characters, THEN THE Book_Service SHALL reject the request with HTTP status 400 and an error message indicating which field exceeds the maximum length
7. WHEN a book creation request is received, THE Book_Service SHALL treat the genre field as optional and store a null value if it is not provided

### Requirement 2: Retrieve Books

**User Story:** As an authenticated user, I want to view all books in my reading log, so that I can see what I have read.

#### Acceptance Criteria

1. WHEN an Authenticated_User requests their book list, THE Book_Controller SHALL respond with HTTP status 200 and a JSON array containing only books belonging to that user, where each book object includes the book's identifier, title, author, genre, and page count
2. WHEN an Authenticated_User has no books in their reading log, THE Book_Controller SHALL respond with HTTP status 200 and an empty JSON array
3. IF the request is missing a valid JWT in the Authorization header, THEN THE system SHALL respond with HTTP status 401 and SHALL NOT return any book data

### Requirement 3: Retrieve a Single Book

**User Story:** As an authenticated user, I want to view the details of a specific book in my reading log, so that I can review its information.

#### Acceptance Criteria

1. WHEN an Authenticated_User requests a book by its identifier, THE Book_Controller SHALL respond with HTTP status 200 and a response body containing the book's identifier, title, author, genre, and page count
2. IF an Authenticated_User requests a book with an identifier that does not correspond to any stored book, THEN THE Book_Controller SHALL respond with HTTP status 404 and a message indicating the book was not found
3. IF an Authenticated_User requests a book that belongs to a different user, THEN THE Book_Controller SHALL respond with HTTP status 404 and a message identical to the not-found response, revealing no indication that the book exists under another user
4. IF an Authenticated_User requests a book using an identifier that is not a valid format, THEN THE Book_Controller SHALL respond with HTTP status 400 and a message indicating the identifier is invalid

### Requirement 4: Update a Book

**User Story:** As an authenticated user, I want to update a book in my reading log, so that I can correct or change its details.

#### Acceptance Criteria

1. WHEN an Authenticated_User submits an update request for an existing book containing a title (1-255 characters), an author (1-255 characters), a genre, and a page count between 1 and 99999, THE Book_Controller SHALL respond with HTTP status 200 and the updated Book
2. IF an Authenticated_User attempts to update a book that does not exist, THEN THE Book_Controller SHALL respond with HTTP status 404 and a message indicating the book was not found
3. IF an Authenticated_User attempts to update a book that belongs to a different user, THEN THE Book_Controller SHALL respond with HTTP status 404 and a message indicating the book was not found
4. IF an update request contains a page count less than 1 or greater than 99999, THEN THE Book_Controller SHALL respond with HTTP status 400 and a message indicating the page count is invalid
5. IF an update request is missing a title or the title exceeds 255 characters, THEN THE Book_Controller SHALL respond with HTTP status 400 and a message indicating the title is invalid
6. IF an update request is missing an author or the author exceeds 255 characters, THEN THE Book_Controller SHALL respond with HTTP status 400 and a message indicating the author is invalid

### Requirement 5: Delete a Book

**User Story:** As an authenticated user, I want to remove a book from my reading log, so that I can keep my list accurate.

#### Acceptance Criteria

1. WHEN an Authenticated_User submits a DELETE request with a book ID as a path variable for a book they own, THE Book_Controller SHALL remove the book record from the database and respond with HTTP status 204 and an empty response body
2. IF an Authenticated_User attempts to delete a book with an ID that does not exist in the database, THEN THE Book_Controller SHALL respond with HTTP status 404 and a response body containing a message indicating the book was not found
3. IF an Authenticated_User attempts to delete a book that belongs to a different user, THEN THE Book_Controller SHALL respond with HTTP status 404 and a response body that is indistinguishable from the response returned when the book does not exist
4. WHEN an Authenticated_User submits a DELETE request for a book they own and the deletion succeeds, THE Book_Controller SHALL ensure that the book is no longer retrievable from the database for any subsequent requests

### Requirement 6: Book Entity Structure

**User Story:** As a developer, I want a well-defined Book entity, so that reading log data is stored consistently.

#### Acceptance Criteria

1. THE Book SHALL have a system-generated unique identifier of type UUID
2. THE Book SHALL store a title as a non-nullable string with a maximum length of 255 characters
3. THE Book SHALL store an author as a non-nullable string with a maximum length of 255 characters
4. THE Book SHALL store a genre as a nullable string with a maximum length of 100 characters
5. THE Book SHALL store a page count as a non-nullable integer with a minimum value of 1 and a maximum value of 50000
6. THE Book SHALL store the owning user's identifier as a non-nullable UUID that references an existing User entity
7. IF a Book is created or updated with a user identifier that does not correspond to an existing User, THEN THE System SHALL reject the operation and return an error indicating an invalid user reference

### Requirement 7: Authentication Enforcement

**User Story:** As a system operator, I want all book endpoints to require authentication, so that reading logs remain private.

#### Acceptance Criteria

1. THE Book_Controller SHALL rely on the existing AuthInterceptor to reject requests with missing, malformed, or invalid JWT tokens before they reach any book endpoint
2. WHILE an Authenticated_User is interacting with book endpoints, THE Book_Service SHALL use only the userId extracted from the JWT subject claim (set as a request attribute by the AuthInterceptor) to scope data access, ensuring that no request can read, modify, or delete book records belonging to a different user
