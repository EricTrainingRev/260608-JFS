package com.example.demo.controller;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestController
@RequiredArgsConstructor
public class BookController {

  private final BookService bookService;

  @PostMapping("/books")
  public ResponseEntity<Book> createBook(@RequestBody Book book, HttpServletRequest request) {
    UUID userId = UUID.fromString((String) request.getAttribute("userId"));
    Book createdBook = bookService.createBook(book, userId);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
  }

  @GetMapping("/books")
  public ResponseEntity<List<Book>> getAllBooks(HttpServletRequest request) {
    UUID userId = UUID.fromString((String) request.getAttribute("userId"));
    List<Book> books = bookService.getAllBooks(userId);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/books/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable UUID id, HttpServletRequest request) {
    UUID userId = UUID.fromString((String) request.getAttribute("userId"));
    Book book = bookService.getBookById(id, userId);
    return ResponseEntity.ok(book);
  }

  @PutMapping("/books/{id}")
  public ResponseEntity<Book> updateBook(
      @PathVariable UUID id, @RequestBody Book book, HttpServletRequest request) {
    UUID userId = UUID.fromString((String) request.getAttribute("userId"));
    Book updatedBook = bookService.updateBook(id, book, userId);
    return ResponseEntity.ok(updatedBook);
  }

  @DeleteMapping("/books/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable UUID id, HttpServletRequest request) {
    UUID userId = UUID.fromString((String) request.getAttribute("userId"));
    bookService.deleteBook(id, userId);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(BookValidationException.class)
  public ResponseEntity<String> handleBookValidationException(BookValidationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(BookNotFoundException.class)
  public ResponseEntity<String> handleBookNotFoundException(BookNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<String> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book identifier is invalid");
  }

  @ExceptionHandler(DataAccessException.class)
  public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An internal error occurred");
  }
}
