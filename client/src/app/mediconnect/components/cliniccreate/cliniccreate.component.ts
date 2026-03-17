import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Clinic } from '../../models/Clinic';
import { MediConnectService } from '../../services/mediconnect.service';

@Component({
  selector: 'app-cliniccreate',
  templateUrl: './cliniccreate.component.html',
  styleUrls: ['./cliniccreate.component.scss']
})
export class ClinicCreateComponent implements OnInit {

  clinicForm!: FormGroup;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private mediconnectService: MediConnectService
  ) {}

  ngOnInit(): void {
    const doctorId = Number(localStorage.getItem('doctor_id')) || 0;
    this.clinicForm = this.formBuilder.group({
      doctor: [{ value: '', disabled: true }],
      clinicId: [null],
      clinicName: ['', [Validators.required, Validators.minLength(2)]],
      location: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      establishedYear: [null, Validators.required],
    });
    this.mediconnectService.getDoctorById(doctorId).subscribe({
      next: () => {},
      error: () => {}
    });
  }

  onSubmit(): void {2
    if (this.clinicForm.valid) {
      const clinic: Clinic = this.clinicForm.getRawValue();
      this.mediconnectService.addClinic(clinic).subscribe({
        next: () => {
          this.errorMessage = null;
          this.successMessage = 'Clinic created successfully!';
        },
        error: () => {
          this.successMessage = null;
          this.errorMessage = 'Bad request. Please check your input.';
        }
      });
    }
  }
}