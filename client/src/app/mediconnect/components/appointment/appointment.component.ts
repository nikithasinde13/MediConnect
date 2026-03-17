// import { Component, OnInit } from '@angular/core';
// import { FormBuilder, FormGroup, Validators } from '@angular/forms';

// @Component({
//     selector: 'app-appointment',
//     templateUrl: './appointment.component.html',
//     styleUrls: ['./appointment.component.scss']
// })
// export class AppointmentCreateComponent implements OnInit {
//     appointmentForm!: FormGroup;
//     successMessage: string | null = null;
//     errorMessage: string | null = null;

//     constructor(private formBuilder: FormBuilder) { }

//     ngOnInit(): void {
//         this.appointmentForm = this.formBuilder.group({
//             appointmentId: [null, [Validators.required, Validators.min(1)]],
//             patientId: [null, [Validators.required, Validators.min(1)]],
//             clinicId: [null, [Validators.required, Validators.min(1)]],
//             appointmentDate: ['', [Validators.required]],
//             status: ['', [Validators.required]],
//             purpose: ['', [Validators.required, Validators.minLength(5)]]
//         });
//     }

//     onSubmit(): void {
//         if (this.appointmentForm.valid) {
//             this.successMessage = 'Appointment has been successfully created!';
//             this.errorMessage = null;
//             console.log('Appointment Created: ', this.appointmentForm.value);
//             this.resetForm();
//         } else {
//             this.errorMessage = 'Please fill out all required fields correctly.';
//             this.successMessage = null;
//         }
//     }

//     resetForm(): void {
//         this.appointmentForm.reset({
//             appointmentId: null,
//             patientId: null,
//             clinicId: null,
//             appointmentDate: '',
//             status: '',
//             purpose: ''
//         });
//     }
// }


import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Appointment } from '../../models/Appointment';
import { Clinic } from '../../models/Clinic';
import { Patient } from '../../models/Patient';
import { MediConnectService } from '../../services/mediconnect.service';

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.scss']
})
export class AppointmentCreateComponent implements OnInit {

  appointmentForm!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  // ✅ FIX 1: Initialize properly
  clinics: Clinic[] = [];
  selectedPatient: Patient | null = null;
  patientId = 0;

  constructor(
    private formBuilder: FormBuilder,
    private mediconnectService: MediConnectService
  ) {}

  ngOnInit(): void {
    this.patientId = Number(localStorage.getItem('patient_id')) || 0;

    // ✅ Load patient safely
    this.mediconnectService.getPatientById(this.patientId).subscribe({
      next: (response) => {
        this.selectedPatient = response;
      },
      error: (error) =>
        console.error('Error loading selectedPatient', error)
    });

    // ✅ Form initialization
    this.appointmentForm = this.formBuilder.group({
      patientId: [{ value: this.patientId, disabled: true }],
      clinic: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      status: ['', Validators.required],
      purpose: ['', [Validators.required, Validators.minLength(5)]]
    });

    // ✅ Load clinics
    this.mediconnectService.getAllClinics().subscribe({
      next: (response) => {
        this.clinics = response;
      },
      error: (error) =>
        console.error('Error loading clinics', error)
    });
  }

  onSubmit(): void {
    if (this.appointmentForm.valid && this.selectedPatient) {

      const appointment: Appointment = {
        ...this.appointmentForm.getRawValue(),
        patient: this.selectedPatient
      };

      this.mediconnectService.createAppointment(appointment).subscribe({
        next: () => {
          this.errorMessage = null;
          this.successMessage = 'Appointment created successfully!';
          this.appointmentForm.reset();
        },
        error: (error) => {
          this.handleError(error);
        }
      });

    } else {
      this.errorMessage = 'Please fill out all required fields correctly.';
      this.successMessage = null;
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