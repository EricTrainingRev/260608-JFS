import { ChangeDetectionStrategy, Component, inject, OnInit, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { BookService } from '../book/book.service';
import { Book } from '../book/book.model';
import { AuthService } from '../auth/auth.service';

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

  books = signal<Book[]>([]);
  errorMessage = signal('');
  editingBookId = signal<string | null>(null);

  bookForm = new FormGroup({
    title: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    author: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    genre: new FormControl('', { nonNullable: true }),
    pageCount: new FormControl(0, { nonNullable: true, validators: [Validators.required, Validators.min(1)] }),
  });

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.getAll().subscribe({
      next: (books) => this.books.set(books),
      error: () => this.errorMessage.set('Failed to load books.'),
    });
  }

  onSubmit(): void {
    this.errorMessage.set('');

    if (this.bookForm.invalid) {
      return;
    }

    const bookData = {
      title: this.bookForm.value.title!,
      author: this.bookForm.value.author!,
      genre: this.bookForm.value.genre || undefined,
      pageCount: this.bookForm.value.pageCount!,
    };

    const editId = this.editingBookId();

    if (editId) {
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

  editBook(book: Book): void {
    this.editingBookId.set(book.id ?? null);
    this.bookForm.setValue({
      title: book.title,
      author: book.author,
      genre: book.genre ?? '',
      pageCount: book.pageCount,
    });
  }

  deleteBook(id: string): void {
    this.bookService.delete(id).subscribe({
      next: () => this.loadBooks(),
      error: () => this.errorMessage.set('Failed to delete book.'),
    });
  }

  cancelEdit(): void {
    this.resetForm();
  }

  logout(): void {
    this.authService.logout();
  }

  private resetForm(): void {
    this.editingBookId.set(null);
    this.bookForm.reset();
  }
}
