import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Patient } from '../../models/Patient';
import { PatientDTO } from '../../models/PatientDTO';
import { User } from '../../models/User';
import { MediConnectService } from '../../services/mediconnect.service';

@Component({
  selector: 'app-patientedit',
  templateUrl: './patientedit.component.html',
  styleUrls: ['./patientedit.component.scss']
})
export class PatientEditComponent implements OnInit {

  patientForm!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  // ✅ FIX 1: Initialize properties
  patientId = 0;
  userId = 0;

  // ✅ FIX 2: Make objects nullable
  patient: Patient | null = null;
  user: User | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private mediconnectService: MediConnectService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.userId = Number(localStorage.getItem('user_id')) || 0;
    this.patientId = Number(this.route.snapshot.paramMap.get('id')) || 0;

    this.patientForm = this.formBuilder.group({
      fullName: ['', [Validators.required, Validators.minLength(2)]],
      dateOfBirth: ['', Validators.required],
      contactNumber: [
        '',
        [Validators.required, Validators.pattern('^[0-9]{10}$')]
      ],
      username: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9]+$/)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[A-Z])(?=.*\d).+$/)
        ]
      ],
      email: [{ value: '', disabled: true }],
      address: ['', [Validators.required, Validators.minLength(5)]]
    });

    this.loadPatientDetails();
  }

  loadPatientDetails(): void {
    this.mediconnectService.getPatientById(this.patientId).subscribe({
      next: (response) => {
        this.patient = response;

        const formattedDate = new Date(response.dateOfBirth)
          .toISOString()
          .split('T')[0];

        this.patientForm.patchValue({
          fullName: response.fullName,
          dateOfBirth: formattedDate,
          contactNumber: response.contactNumber,
          email: response.email,
          address: response.address
        });
      },
      error: (error) =>
        console.error('Error loading patient details:', error)
    });

    this.mediconnectService.getUserById(this.userId).subscribe({
      next: (response) => {
        this.user = response;

        this.patientForm.patchValue({
          username: response.username,
          password: response.password
        });
      },
      error: (error) =>
        console.error('Error loading user details:', error)
    });
  }

  onSubmit(): void {
    if (this.patientForm.valid) {

      const patient: PatientDTO = {
        ...this.patientForm.getRawValue(),
        patientId: this.patientId
      };

      this.mediconnectService.updatePatient(patient).subscribe({
        next: () => {
          this.errorMessage = null;
          this.successMessage = 'Patient updated successfully!';
          this.patientForm.reset();
        },
        error: (error) => {
          this.handleError(error);
        }
      });
    }
  }

  private handleError(error: HttpErrorResponse): void {
    if (error.error instanceof ErrorEvent) {
      this.errorMessage = `Client-side error: ${error.error.message}`;
    } else {
      if (error.status === 400) {
        this.errorMessage = 'Bad request. Please check your input.';
      } else {
        this.errorMessage = `Server-side error: ${error.status} ${error.message}`;
      }
    }
    this.successMessage = null;
  }
}