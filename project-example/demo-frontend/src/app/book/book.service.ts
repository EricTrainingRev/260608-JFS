import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from './book.model';

@Injectable({ providedIn: 'root' })
export class BookService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/books';

  getAll(): Observable<Book[]> {
    return this.http.get<Book[]>(this.baseUrl);
  }

  getById(id: string): Observable<Book> {
    return this.http.get<Book>(`${this.baseUrl}/${id}`);
  }

  create(book: Omit<Book, 'id'>): Observable<Book> {
    return this.http.post<Book>(this.baseUrl, book);
  }

  update(id: string, book: Omit<Book, 'id'>): Observable<Book> {
    return this.http.put<Book>(`${this.baseUrl}/${id}`, book);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
