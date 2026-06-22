import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);

  // Signal holding the current JWT (null when not authenticated)
  token = signal<string | null>(this.getStoredToken());

  // Derived signal for quick auth checks
  isAuthenticated = computed(() => this.token() !== null);

  login(username: string, password: string) {
    return this.http.post('http://localhost:8080/login', { username, password }, { responseType: 'text' });
  }

  /** Store the token in memory and localStorage */
  setToken(token: string): void {
    this.token.set(token);
    localStorage.setItem('auth_token', token);
  }

  /** Clear auth state and redirect to login */
  logout(): void {
    this.token.set(null);
    localStorage.removeItem('auth_token');
    this.router.navigate(['/login']);
  }

  /** Retrieve token from localStorage on app startup */
  private getStoredToken(): string | null {
    return localStorage.getItem('auth_token');
  }
}
