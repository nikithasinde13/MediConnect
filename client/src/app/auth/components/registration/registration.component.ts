import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss'],
})
export class RegistrationComponent implements OnInit {
  registrationForm!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;
  selectedRole: string | null = null;
  constructor(private formBuilder: FormBuilder, private authService: AuthService) {}
  ngOnInit(): void {
    this.registrationForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]+$/)]],
      password: ['', [Validators.required, Validators.pattern(/^(?=.*[A-Z])(?=.*\d).{8,}$/)]],
      role: ['', [Validators.required]],
      fullName: ['', [Validators.required, Validators.minLength(2)]],
      contactNumber: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      email: ['', [Validators.required, Validators.email]],
      specialty: [''],
      yearsOfExperience: [''],
      dateOfBirth: [''],
      address: [''],
    });
  }
  onRoleChange(event: Event): void {
    const role = (event.target as HTMLSelectElement).value;
    this.selectedRole = role;
    const specialty = this.registrationForm.get('specialty') as AbstractControl;
    const yearsOfExperience = this.registrationForm.get('yearsOfExperience') as AbstractControl;
    const dateOfBirth = this.registrationForm.get('dateOfBirth') as AbstractControl;
    const address = this.registrationForm.get('address') as AbstractControl;
    specialty.clearValidators();
    yearsOfExperience.clearValidators();
    dateOfBirth.clearValidators();
    address.clearValidators();
    if (role === 'DOCTOR') {
      specialty.setValidators([Validators.required]);
      yearsOfExperience.setValidators([Validators.required, Validators.min(1)]);
    } else if (role === 'PATIENT') {
      dateOfBirth.setValidators([Validators.required]);
      address.setValidators([Validators.required, Validators.minLength(5)]);
    }
    specialty.updateValueAndValidity();
    yearsOfExperience.updateValueAndValidity();
    dateOfBirth.updateValueAndValidity();
    address.updateValueAndValidity();
  }
  onSubmit(): void {
    if (this.registrationForm.invalid) {
      this.successMessage = null;
      this.errorMessage = 'Invalid form';
      return;
    }
    const payload = this.registrationForm.value;
    this.authService.createUser(payload).subscribe({
      next: () => {
        this.errorMessage = null;
        this.successMessage = 'Registration successful';
        this.registrationForm.reset();
        this.selectedRole = null;
      },
      error: () => {
        this.successMessage = null;
        this.errorMessage = 'Registration failed';
      },
    });
  }
  resetForm(): void {
    this.registrationForm.reset();
    this.selectedRole = null;
    this.successMessage = null;
    this.errorMessage = null;
  }
}
