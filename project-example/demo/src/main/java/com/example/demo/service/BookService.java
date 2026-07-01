package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.repo.BookRepo;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookService {

  private final BookRepo bookRepo;

  public Book createBook(Book book, UUID userId) {
    validateTitle(book.getTitle());
    validateAuthor(book.getAuthor());
    validateGenre(book.getGenre());
    validatePageCountForCreate(book.getPageCount());

    User user = new User();
    user.setId(userId);
    book.setUser(user);

    return bookRepo.save(book);
  }

  public List<Book> getAllBooks(UUID userId) {
    return bookRepo.findAllByUserId(userId);
  }

  public Book getBookById(UUID bookId, UUID userId) {
    return bookRepo
        .findByIdAndUserId(bookId, userId)
        .orElseThrow(() -> new BookNotFoundException("Book not found"));
  }

  public Book updateBook(UUID bookId, Book updatedBook, UUID userId) {
    Book existingBook =
        bookRepo
            .findByIdAndUserId(bookId, userId)
            .orElseThrow(() -> new BookNotFoundException("Book not found"));

    validateTitle(updatedBook.getTitle());
    validateAuthor(updatedBook.getAuthor());
    validateGenre(updatedBook.getGenre());
    validatePageCountForUpdate(updatedBook.getPageCount());

    existingBook.setTitle(updatedBook.getTitle());
    existingBook.setAuthor(updatedBook.getAuthor());
    existingBook.setGenre(updatedBook.getGenre());
    existingBook.setPageCount(updatedBook.getPageCount());

    return bookRepo.save(existingBook);
  }

  public void deleteBook(UUID bookId, UUID userId) {
    Book book =
        bookRepo
            .findByIdAndUserId(bookId, userId)
            .orElseThrow(() -> new BookNotFoundException("Book not found"));
    bookRepo.delete(book);
  }

  private void validateTitle(String title) {
    if (title == null || title.isBlank()) {
      throw new BookValidationException("Title is required");
    }
    if (title.length() > 255) {
      throw new BookValidationException("Title must not exceed 255 characters");
    }
  }

  private void validateAuthor(String author) {
    if (author == null || author.isBlank()) {
      throw new BookValidationException("Author is required");
    }
    if (author.length() > 255) {
      throw new BookValidationException("Author must not exceed 255 characters");
    }
  }

  private void validateGenre(String genre) {
    if (genre != null && genre.length() > 100) {
      throw new BookValidationException("Genre must not exceed 100 characters");
    }
  }

  private void validatePageCountForCreate(int pageCount) {
    if (pageCount < 1 || pageCount > 25000) {
      throw new BookValidationException("Page count must be between 1 and 25000");
    }
  }

  private void validatePageCountForUpdate(int pageCount) {
    if (pageCount < 1 || pageCount > 99999) {
      throw new BookValidationException("Page count must be between 1 and 99999");
    }
  }
}
