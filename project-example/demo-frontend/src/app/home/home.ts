import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BookService } from '../book/book.service';
import { Book } from '../book/book.model';
import { AuthService } from '../auth/auth.service';

/**
 * Home component — the main authenticated view of the application.
 *
 * Responsibilities:
 * - Displays all books in a table.
 * - Provides a reactive form for creating and updating books.
 * - Handles edit/delete actions per book row.
 * - Allows the user to log out.
 *
 * Uses OnPush change detection with signals so the view only re-renders
 * when signal values actually change.
 */
@Component({
  selector: 'app-home',
  imports: [ReactiveFormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Home implements OnInit {
  private bookService = inject(BookService);
  private authService = inject(AuthService);

  // --- Component state (signals) ---

  /** The list of books fetched from the API */
  books = signal<Book[]>([]);

  /** User-facing error message displayed when an operation fails */
  errorMessage = signal('');

  /**
   * Tracks which book is currently being edited.
   * null = form is in "create" mode; a string ID = form is in "update" mode.
   */
  editingBookId = signal<string | null>(null);

  // --- Reactive form definition ---

  /** Form group for creating/editing a book. All fields are non-nullable with validators. */
  bookForm = new FormGroup({
    title: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    author: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    genre: new FormControl('', { nonNullable: true }),
    pageCount: new FormControl(0, { nonNullable: true, validators: [Validators.required, Validators.min(1)] }),
  });

  // --- Lifecycle ---

  /** Load the book list as soon as the component initializes */
  ngOnInit(): void {
    this.loadBooks();
  }

  // --- Data operations ---

  /** Fetches all books from the backend and updates the signal */
  loadBooks(): void {
    this.bookService.getAll().subscribe({
      next: (books) => this.books.set(books),
      error: () => this.errorMessage.set('Failed to load books.'),
    });
  }

  /**
   * Handles form submission for both create and update flows.
   * Determines which operation to perform based on `editingBookId`.
   */
  onSubmit(): void {
    this.errorMessage.set('');

    // Don't submit if validation fails
    if (this.bookForm.invalid) {
      return;
    }

    // Build the payload from form values
    const bookData = {
      title: this.bookForm.value.title!,
      author: this.bookForm.value.author!,
      genre: this.bookForm.value.genre || undefined,
      pageCount: this.bookForm.value.pageCount!,
    };

    const editId = this.editingBookId();

    if (editId) {
      // --- Update existing book ---
      this.bookService.update(editId, bookData).subscribe({
        next: () => {
          this.resetForm();
          this.loadBooks();
        },
        error: (err) => {
          const message = typeof err.error === 'string' ? err.error : 'Failed to update book.';
          this.errorMessage.set(message);
        },
      });
    } else {
      // --- Create new book ---
      this.bookService.create(bookData).subscribe({
        next: () => {
          this.resetForm();
          this.loadBooks();
        },
        error: (err) => {
          const message = typeof err.error === 'string' ? err.error : 'Failed to create book.';
          this.errorMessage.set(message);
        },
      });
    }
  }

  // --- UI actions ---

  /** Populate the form with an existing book's data for editing */
  editBook(book: Book): void {
    this.editingBookId.set(book.id ?? null);
    this.bookForm.setValue({
      title: book.title,
      author: book.author,
      genre: book.genre ?? '',
      pageCount: book.pageCount,
    });
  }

  /** Delete a book by ID, then refresh the list */
  deleteBook(id: string): void {
    this.bookService.delete(id).subscribe({
      next: () => this.loadBooks(),
      error: () => this.errorMessage.set('Failed to delete book.'),
    });
  }

  /** Exit edit mode without saving */
  cancelEdit(): void {
    this.resetForm();
  }

  /** Log the user out and redirect to login (delegated to AuthService) */
  logout(): void {
    this.authService.logout();
  }

  // --- Private helpers ---

  /** Clear the form and reset edit mode back to "create" */
  private resetForm(): void {
    this.editingBookId.set(null);
    this.bookForm.reset();
  }
}
