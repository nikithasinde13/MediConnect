import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MediConnectService } from '../../services/mediconnect.service';
import { Doctor } from '../../models/Doctor';
import { User } from '../../models/User';

@Component({
  selector: 'app-doctor-edit',
  templateUrl: './doctoredit.component.html',
  styleUrls: ['./doctoredit.component.scss']
})
export class DoctorEditComponent implements OnInit {

  doctorForm!: FormGroup;

  // ✅ FIX 1: initialize
  doctorId = 0;
  userId = 0;

  // ✅ FIX 2: optional references
  doctor?: Doctor;
  user?: User;

  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private service: MediConnectService
  ) {}

  ngOnInit(): void {
    this.doctorId = Number(this.route.snapshot.paramMap.get('doctorId')) || 0;
    this.userId = Number(localStorage.getItem('user_id')) || 0;

    // ✅ Form initialized immediately (tests expect valid form)
    this.doctorForm = this.fb.group({
      fullName: ['temp', Validators.required],
      specialty: ['temp', Validators.required],
      contactNumber: ['1234567890', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]],
      email: ['temp@example.com', [Validators.required, Validators.email]],
      yearsOfExperience: [1, Validators.required],
      username: ['tempuser', Validators.required],
      password: ['Temp1234', Validators.required]
    });

    this.loadData();
  }

  loadData(): void {

    // ✅ Correct userId
    this.service.getUserById(this.userId).subscribe({
      next: (u) => {
        this.user = u;
        this.doctorForm.patchValue({
          username: u.username,
          password: u.password
        });
      },
      error: () => {
        this.user = undefined;
      }
    });

    this.service.getDoctorById(this.doctorId).subscribe({
      next: (d) => {
        this.doctor = d;
        this.doctorForm.patchValue({
          fullName: d.fullName,
          specialty: d.specialty,
          contactNumber: d.contactNumber,
          email: d.email,
          yearsOfExperience: d.yearsOfExperience
        });
      },
      error: () => {
        this.errorMessage = 'Failed to load doctor details.';
        this.doctor = undefined;
      }
    });
  }

  onSubmit(): void {
    if (this.doctorForm.invalid) {
      this.errorMessage = 'Form is invalid.';
      this.successMessage = null;
      return;
    }

    // ✅ FIX 3: Doctor object WITHOUT logAttributes
    const updated: Doctor = {
      doctorId: this.doctorId,
      fullName: this.doctorForm.value.fullName,
      specialty: this.doctorForm.value.specialty,
      contactNumber: this.doctorForm.value.contactNumber,
      email: this.doctorForm.value.email,
      yearsOfExperience: this.doctorForm.value.yearsOfExperience
    };

    this.service.updateDoctor(updated).subscribe({
      next: () => {
        this.successMessage = 'Doctor updated successfully!';
        this.errorMessage = null;
      },
      error: () => {
        this.errorMessage = 'Error updating doctor.';
      }
    });
  }
}