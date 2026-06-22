import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Register {
  private http = inject(HttpClient);

  registerForm = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
  });

  successMessage = signal('');
  errorMessage = signal('');

  onSubmit(): void {
    this.successMessage.set('');
    this.errorMessage.set('');

    const body = {
      username: this.registerForm.value.username,
      password: this.registerForm.value.password,
    };

    this.http.post('http://localhost:8080/register', body, { observe: 'response', responseType: 'text' }).subscribe({
      next: (response) => {
        if (response.status === 201) {
          this.successMessage.set('Registration successful!');
        }
      },
      error: (err) => {
        const message = typeof err.error === 'string' ? err.error : (err.error?.message ?? 'An unexpected error occurred');
        this.errorMessage.set(message);
      },
    });
  }
}
