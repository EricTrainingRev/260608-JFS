import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './book.model';

/**
 * Service responsible for all Book-related HTTP communication.
 *
 * Provides standard CRUD operations against the backend REST API.
 * Each method returns an Observable — the caller subscribes to trigger
 * the request and receive the response.
 *
 * The auth token is attached automatically by `authInterceptor`,
 * so this service doesn't need to handle authentication headers.
 */
@Injectable({ providedIn: 'root' })
export class BookService {
  private http = inject(HttpClient);

  // Base URL for the books REST endpoint
  private baseUrl = 'http://localhost:8080/books';

  /** Fetch all books (that belong to the user) from the API */
  getAll(): Observable<Book[]> {
    return this.http.get<Book[]>(this.baseUrl);
  }

  /** Fetch a single book by its ID */
  getById(id: string): Observable<Book> {
    return this.http.get<Book>(`${this.baseUrl}/${id}`);
  }

  /**
   * Create a new book.
   * Uses Omit<Book, 'id'> because the backend generates the ID.
   */
  create(book: Omit<Book, 'id'>): Observable<Book> {
    return this.http.post<Book>(this.baseUrl, book);
  }

  /**
   * Replace an existing book's data.
   * Uses PUT for a full replacement — all fields must be provided.
   */
  update(id: string, book: Omit<Book, 'id'>): Observable<Book> {
    return this.http.put<Book>(`${this.baseUrl}/${id}`, book);
  }

  /** Delete a book by ID. Returns void — no response body expected. */
  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
