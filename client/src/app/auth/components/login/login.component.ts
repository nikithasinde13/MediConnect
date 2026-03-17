import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
    templateUrl: './login.component.html',
      styleUrls: ['./login.component.scss'],
      })
      export class LoginComponent implements OnInit {
        loginForm!: FormGroup;
          errorMessage: string | null = null;
            successMessage: string | null = null;
              constructor(private formBuilder: FormBuilder, private authService: AuthService, private router: Router) {}
                ngOnInit(): void {
                    this.loginForm = this.formBuilder.group({
                          username: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]+$/)]],
                                password: ['', [Validators.required, Validators.minLength(8)]],
                                    });
                                      }
                                        onSubmit(): void {
                                            if (this.loginForm.invalid) {
                                                  this.errorMessage = 'Invalid form';
                                                        this.successMessage = null;
                                                              return;
                                                                  }
                                                                      const { username, password } = this.loginForm.value;
                                                                          this.authService.login({ username, password }).subscribe({
                                                                                next: (res) => {
                                                                                        const token = res['token'] || '';
                                                                                                const roles = res['roles'] || res['role'] || '';
                                                                                                        const userId = res['userId'] || res['user_id'] || '';
                                                                                                                const doctorId = res['doctorId'] || res['doctor_id'] || '';
                                                                                                                        const patientId = res['patientId'] || res['patient_id'] || '';
                                                                                                                                if (token) localStorage.setItem('token', token);
                                                                                                                                        if (roles) localStorage.setItem('role', roles);
                                                                                                                                                if (userId) localStorage.setItem('user_id', String(userId));
                                                                                                                                                        if (doctorId) localStorage.setItem('doctor_id', String(doctorId));
                                                                                                                                                                if (patientId) localStorage.setItem('patient_id', String(patientId));
                                                                                                                                                                        this.errorMessage = null;
                                                                                                                                                                                this.successMessage = 'Login successful';
                                                                                                                                                                                      },
                                                                                                                                                                                            error: () => {
                                                                                                                                                                                                    this.successMessage = null;
                                                                                                                                                                                                            this.errorMessage = 'Login failed';
                                                                                                                                                                                                                  },
                                                                                                                                                                                                                      });
                                                                                                                                                                                                                        }
                                                                                                                                                                                                                        }

                                                                                                                                                                                                                        