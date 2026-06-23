import { Component, inject, OnInit } from '@angular/core';
import { LoginService } from '../../service/login-service';
import { User } from '../../interface/user';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  JWT: User | any;
  errorMessage: string | null = null;
  loginForm!: FormGroup;
  isSubmitted = false;

  private formBuilder = inject(FormBuilder);

  private loginService = inject(LoginService);

  private router = inject(Router);

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(5)]],
      password: ['', [Validators.required, Validators.minLength(5)]],
    });
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.isSubmitted = true;

    if (this.loginForm.invalid) {
      return;
    }

    console.log('Login Data Successfully Submitted:', this.loginForm.value);
    // Execute backend API service transmission layer here
    // Execute the POST request and handle response

    const existingUser: User = {
      username: this.loginForm.value.username,
      password: this.loginForm.value.password,
    };

    this.loginService.loginUser(existingUser).subscribe({
      next: (response) => {
        this.JWT = response;
        console.log('User logged in successfully:', response);
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        this.errorMessage = 'Failed to login user.';
        console.error('API Error:', error);
      }
    });
  };
}
