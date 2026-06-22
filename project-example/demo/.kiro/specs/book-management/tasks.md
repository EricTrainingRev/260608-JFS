# Implementation Plan: Book Management

## Overview

This plan implements full CRUD book management for the Summer Reading Tracker. We follow the existing layered architecture (entity → repo → service → controller) and mirror conventions from the User feature. Tasks are ordered so each step builds on the previous, ending with integration wiring and final verification.

## Tasks

- [ ] 1. Create Book entity and repository layer
  - [ ] 1.1 Create the Book entity class
    - Create `src/main/java/com/example/demo/entity/Book.java`
    - Use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`)
    - Define fields: `id` (UUID, auto-generated), `title` (String, non-null, max 255), `author` (String, non-null, max 255), `genre` (String, nullable, max 100), `pageCount` (int, non-null)
    - Add `@ManyToOne(fetch = FetchType.LAZY)` relationship to User with `@JoinColumn(name = "user_id", nullable = false)`
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

  - [ ] 1.2 Create the BookRepo interface
    - Create `src/main/java/com/example/demo/repo/BookRepo.java`
    - Extend `JpaRepository<Book, UUID>`
    - Add `List<Book> findAllByUserId(UUID userId)` query method
    - Add `Optional<Book> findByIdAndUserId(UUID id, UUID userId)` query method
    - _Requirements: 2.1, 3.1, 7.2_

- [ ] 2. Create custom exceptions
  - [ ] 2.1 Create BookNotFoundException
    - Create `src/main/java/com/example/demo/exception/BookNotFoundException.java`
    - Extend `RuntimeException` with a message constructor
    - _Requirements: 3.2, 4.2, 5.2_

  - [ ] 2.2 Create BookValidationException
    - Create `src/main/java/com/example/demo/exception/BookValidationException.java`
    - Extend `RuntimeException` with a message constructor
    - _Requirements: 1.3, 1.4, 1.5, 1.6_

- [ ] 3. Implement BookService with validation and business logic
  - [ ] 3.1 Create BookService class with create and read operations
    - Create `src/main/java/com/example/demo/service/BookService.java`
    - Annotate with `@Service` and `@RequiredArgsConstructor`
    - Inject `BookRepo` as a final field
    - Implement `createBook(Book book, UUID userId)`: validate title (non-null, non-blank, max 255), author (non-null, non-blank, max 255), genre (max 100 if provided), pageCount (1–25000); set user association; save and return
    - Implement `getAllBooks(UUID userId)`: delegate to `bookRepo.findAllByUserId(userId)`
    - Implement `getBookById(UUID bookId, UUID userId)`: use `findByIdAndUserId`, throw `BookNotFoundException` if empty
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 2.1, 2.2, 3.1, 3.2, 3.3, 7.2_

  - [ ] 3.2 Add update and delete operations to BookService
    - Implement `updateBook(UUID bookId, Book updatedBook, UUID userId)`: find by ID and userId (throw `BookNotFoundException` if missing), validate title (non-blank, max 255), author (non-blank, max 255), genre (max 100 if provided), pageCount (1–99999); update fields and save
    - Implement `deleteBook(UUID bookId, UUID userId)`: find by ID and userId (throw `BookNotFoundException` if missing), delete
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 5.1, 5.2, 5.3, 5.4_

  - [ ]* 3.3 Write unit tests for BookService
    - Create `src/test/java/com/example/demo/service/BookServiceTest.java`
    - Test successful book creation with valid inputs
    - Test rejection of blank/null title and author
    - Test rejection of out-of-range page counts (create: outside 1–25000, update: outside 1–99999)
    - Test rejection of fields exceeding max length
    - Test that `getBookById` throws `BookNotFoundException` when book not found or owned by different user
    - Test that `deleteBook` throws `BookNotFoundException` for non-existent or non-owned book
    - Use H2 in-memory database for repository interactions
    - _Requirements: 1.3, 1.4, 1.5, 1.6, 3.2, 3.3, 4.2, 4.3, 4.4, 4.5, 4.6, 5.2, 5.3_

- [ ] 4. Checkpoint - Ensure service layer tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [ ] 5. Implement BookController with REST endpoints
  - [ ] 5.1 Create BookController with create and read endpoints
    - Create `src/main/java/com/example/demo/controller/BookController.java`
    - Annotate with `@RestController` and `@RequiredArgsConstructor`
    - Inject `BookService` as a final field
    - Implement `@PostMapping("/books")` — extract `userId` from `request.getAttribute("userId")`, parse to UUID, delegate to service, return 201 with created book
    - Implement `@GetMapping("/books")` — extract `userId`, delegate to service, return 200 with list
    - Implement `@GetMapping("/books/{id}")` — extract `userId`, delegate to service with path variable UUID, return 200 with book
    - _Requirements: 1.1, 1.2, 2.1, 2.2, 2.3, 3.1, 3.4, 7.1_

  - [ ] 5.2 Add update and delete endpoints to BookController
    - Implement `@PutMapping("/books/{id}")` — extract `userId`, delegate to service, return 200 with updated book
    - Implement `@DeleteMapping("/books/{id}")` — extract `userId`, delegate to service, return 204 with empty body
    - _Requirements: 4.1, 5.1, 5.4, 7.1_

  - [ ] 5.3 Add exception handlers to BookController
    - Add `@ExceptionHandler(BookValidationException.class)` returning 400 with error message
    - Add `@ExceptionHandler(BookNotFoundException.class)` returning 404 with "Book not found"
    - Add `@ExceptionHandler(MethodArgumentTypeMismatchException.class)` returning 400 with "Book identifier is invalid" (handles invalid UUID format in path)
    - Add `@ExceptionHandler(DataAccessException.class)` returning 500 with generic error message
    - _Requirements: 1.3, 1.4, 1.5, 1.6, 3.2, 3.3, 3.4, 4.2, 4.3, 4.4, 4.5, 4.6, 5.2, 5.3_

  - [ ]* 5.4 Write unit tests for BookController
    - Create `src/test/java/com/example/demo/controller/BookControllerTest.java`
    - Use `@WebMvcTest(BookController.class)` with mocked `BookService`
    - Test POST `/books` returns 201 with created book
    - Test GET `/books` returns 200 with list of books
    - Test GET `/books/{id}` returns 200 for valid owned book
    - Test GET `/books/{id}` returns 404 when `BookNotFoundException` is thrown
    - Test PUT `/books/{id}` returns 200 with updated book
    - Test DELETE `/books/{id}` returns 204
    - Test invalid UUID path variable returns 400
    - Test validation failure returns 400
    - _Requirements: 1.1, 2.1, 3.1, 3.2, 3.4, 4.1, 5.1_

- [ ] 6. Checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

- [x] 7. Create test configuration for H2
  - [x] 7.1 Create test.properties for H2 in-memory database
    - Create `src/test/resources/test.properties` (or `application-test.properties` depending on test setup)
    - Configure H2 datasource URL, driver, dialect, and ddl-auto settings for the test environment
    - Ensure tests use H2 instead of SQLite
    - _Requirements: (infrastructure — supports all test tasks)_

- [ ] 8. Property-based tests for correctness properties
  - [ ]* 8.1 Write property test for valid creation round-trip
    - **Property 1: Valid creation round-trip**
    - For any valid book payload, creating the book should return a non-null UUID and the persisted book's userId should equal the authenticated user's ID
    - **Validates: Requirements 1.1, 1.2, 6.1**

  - [ ]* 8.2 Write property test for blank required fields rejection
    - **Property 2: Blank required fields are rejected**
    - For any whitespace-only or null title/author, the service should reject with a validation exception
    - **Validates: Requirements 1.3, 1.4, 4.5, 4.6**

  - [ ]* 8.3 Write property test for out-of-range fields rejection on creation
    - **Property 3: Out-of-range or oversized fields are rejected on creation**
    - For any page count outside [1, 25000] or title/author exceeding 255 chars, the service should reject
    - **Validates: Requirements 1.5, 1.6**

  - [ ]* 8.4 Write property test for ownership scoping
    - **Property 4: Ownership scoping — users see only their own books**
    - For any set of books across users, retrieving a user's list returns only their books
    - **Validates: Requirements 2.1, 7.2**

  - [ ]* 8.5 Write property test for cross-user access denial
    - **Property 5: Cross-user access is indistinguishable from not-found**
    - For any book owned by user A, user B accessing it gets 404 identical to a non-existent book
    - **Validates: Requirements 3.3, 4.3, 5.3**

  - [ ]* 8.6 Write property test for out-of-range fields rejection on update
    - **Property 6: Out-of-range fields are rejected on update**
    - For any update with page count outside [1, 99999] or blank/oversized title/author, the service should reject and leave the book unchanged
    - **Validates: Requirements 4.4, 4.5, 4.6**

  - [ ]* 8.7 Write property test for create-then-read and update-then-read round-trips
    - **Property 7: Create-then-read and update-then-read round-trips**
    - After creation, fetching by ID returns matching fields; after update, fetching returns updated values
    - **Validates: Requirements 3.1, 4.1**

  - [ ]* 8.8 Write property test for delete permanence
    - **Property 8: Delete permanence**
    - After successful deletion (204), any subsequent fetch of that book returns 404
    - **Validates: Requirements 5.1, 5.4**

  - [ ]* 8.9 Write property test for non-existent book ID
    - **Property 9: Non-existent book ID returns 404**
    - For any random UUID not in the database, read/update/delete should return 404
    - **Validates: Requirements 3.2, 4.2, 5.2**

- [ ] 9. Final checkpoint - Ensure all tests pass
  - Ensure all tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- The existing `WebConfig` already applies `AuthInterceptor` to all paths except `/register` and `/login`, so `/books/**` endpoints are automatically protected — no additional interceptor configuration needed
- Property tests validate universal correctness properties from the design document
- Unit tests validate specific examples and edge cases
- Test configuration (task 7.1) should be created early if tests are run incrementally; it is placed after the controller for logical grouping but can be done alongside task 3.3

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "2.1", "2.2", "7.1"] },
    { "id": 1, "tasks": ["1.2"] },
    { "id": 2, "tasks": ["3.1"] },
    { "id": 3, "tasks": ["3.2", "3.3"] },
    { "id": 4, "tasks": ["5.1"] },
    { "id": 5, "tasks": ["5.2", "5.3"] },
    { "id": 6, "tasks": ["5.4", "8.1", "8.2", "8.3"] },
    { "id": 7, "tasks": ["8.4", "8.5", "8.6", "8.7", "8.8", "8.9"] }
  ]
}
```
