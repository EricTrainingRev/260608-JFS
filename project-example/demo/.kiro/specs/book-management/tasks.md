# Implementation Plan: Book Management

## Overview

This plan implements full CRUD book management for the Summer Reading Tracker. We follow the existing layered architecture (entity → repo → service → controller) and mirror conventions from the User feature. Tasks are ordered so each step builds on the previous, ending with integration wiring.

## Tasks

- [x] 1. Create Book entity and repository layer
  - [x] 1.1 Create the Book entity class
    - Create `src/main/java/com/example/demo/entity/Book.java`
    - Use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`)
    - Define fields: `id` (UUID, auto-generated), `title` (String, non-null, max 255), `author` (String, non-null, max 255), `genre` (String, nullable, max 100), `pageCount` (int, non-null)
    - Add `@ManyToOne(fetch = FetchType.LAZY)` relationship to User with `@JoinColumn(name = "user_id", nullable = false)`
    - _Requirements: 6.1, 6.2, 6.3, 6.4, 6.5, 6.6_

  - [x] 1.2 Create the BookRepo interface
    - Create `src/main/java/com/example/demo/repo/BookRepo.java`
    - Extend `JpaRepository<Book, UUID>`
    - Add `List<Book> findAllByUserId(UUID userId)` query method
    - Add `Optional<Book> findByIdAndUserId(UUID id, UUID userId)` query method
    - _Requirements: 2.1, 3.1, 7.2_

- [x] 2. Create custom exceptions
  - [x] 2.1 Create BookNotFoundException
    - Create `src/main/java/com/example/demo/exception/BookNotFoundException.java`
    - Extend `RuntimeException` with a message constructor
    - _Requirements: 3.2, 4.2, 5.2_

  - [x] 2.2 Create BookValidationException
    - Create `src/main/java/com/example/demo/exception/BookValidationException.java`
    - Extend `RuntimeException` with a message constructor
    - _Requirements: 1.3, 1.4, 1.5, 1.6_

- [x] 3. Implement BookService with validation and business logic
  - [x] 3.1 Create BookService class with create and read operations
    - Create `src/main/java/com/example/demo/service/BookService.java`
    - Annotate with `@Service` and `@RequiredArgsConstructor`
    - Inject `BookRepo` as a final field
    - Implement `createBook(Book book, UUID userId)`: validate title (non-null, non-blank, max 255), author (non-null, non-blank, max 255), genre (max 100 if provided), pageCount (1–25000); set user association; save and return
    - Implement `getAllBooks(UUID userId)`: delegate to `bookRepo.findAllByUserId(userId)`
    - Implement `getBookById(UUID bookId, UUID userId)`: use `findByIdAndUserId`, throw `BookNotFoundException` if empty
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 2.1, 2.2, 3.1, 3.2, 3.3, 7.2_

  - [x] 3.2 Add update and delete operations to BookService
    - Implement `updateBook(UUID bookId, Book updatedBook, UUID userId)`: find by ID and userId (throw `BookNotFoundException` if missing), validate title (non-blank, max 255), author (non-blank, max 255), genre (max 100 if provided), pageCount (1–99999); update fields and save
    - Implement `deleteBook(UUID bookId, UUID userId)`: find by ID and userId (throw `BookNotFoundException` if missing), delete
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 5.1, 5.2, 5.3, 5.4_

- [x] 4. Implement BookController with REST endpoints
  - [x] 4.1 Create BookController with create and read endpoints
    - Create `src/main/java/com/example/demo/controller/BookController.java`
    - Annotate with `@RestController` and `@RequiredArgsConstructor`
    - Inject `BookService` as a final field
    - Implement `@PostMapping("/books")` — extract `userId` from `request.getAttribute("userId")`, parse to UUID, delegate to service, return 201 with created book
    - Implement `@GetMapping("/books")` — extract `userId`, delegate to service, return 200 with list
    - Implement `@GetMapping("/books/{id}")` — extract `userId`, delegate to service with path variable UUID, return 200 with book
    - _Requirements: 1.1, 1.2, 2.1, 2.2, 2.3, 3.1, 3.4, 7.1_

  - [x] 4.2 Add update and delete endpoints to BookController
    - Implement `@PutMapping("/books/{id}")` — extract `userId`, delegate to service, return 200 with updated book
    - Implement `@DeleteMapping("/books/{id}")` — extract `userId`, delegate to service, return 204 with empty body
    - _Requirements: 4.1, 5.1, 5.4, 7.1_

  - [x] 4.3 Add exception handlers to BookController
    - Add `@ExceptionHandler(BookValidationException.class)` returning 400 with error message
    - Add `@ExceptionHandler(BookNotFoundException.class)` returning 404 with "Book not found"
    - Add `@ExceptionHandler(MethodArgumentTypeMismatchException.class)` returning 400 with "Book identifier is invalid" (handles invalid UUID format in path)
    - Add `@ExceptionHandler(DataAccessException.class)` returning 500 with generic error message
    - _Requirements: 1.3, 1.4, 1.5, 1.6, 3.2, 3.3, 3.4, 4.2, 4.3, 4.4, 4.5, 4.6, 5.2, 5.3_

## Notes

- Each task references specific requirements for traceability
- The existing `WebConfig` already applies `AuthInterceptor` to all paths except `/register` and `/login`, so `/books/**` endpoints are automatically protected — no additional interceptor configuration needed

## Task Dependency Graph

```json
{
  "waves": [
    { "id": 0, "tasks": ["1.1", "2.1", "2.2"] },
    { "id": 1, "tasks": ["1.2"] },
    { "id": 2, "tasks": ["3.1"] },
    { "id": 3, "tasks": ["3.2"] },
    { "id": 4, "tasks": ["4.1"] },
    { "id": 5, "tasks": ["4.2", "4.3"] }
  ]
}
```
