// import { Component, OnInit } from '@angular/core';
// import { Router } from '@angular/router';
// import { Appointment } from '../../models/Appointment';
// import { Clinic } from '../../models/Clinic';
// import { Doctor } from '../../models/Doctor';
// import { Patient } from '../../models/Patient';
// import { MediConnectService } from '../../services/mediconnect.service';

// @Component({
//   selector: 'app-dashboard',
//   templateUrl: './dashboard.component.html',
//   styleUrls: ['./dashboard.component.scss'],
// })
// export class DashboardComponent implements OnInit {

//   // ✅ MUST be undefined for test
//   doctorDetails: Doctor | undefined;

//   clinics: Clinic[] = [];
//   appointments: Appointment[] = [];
//   patients: Patient[] = [];

//   role: string | null = null;
//   userId = 0;
//   doctorId = 0;
//   patientId = 0;

//   selectedClinicId?: number;
//   selectClinicAppointments: Appointment[] = [];

//   constructor(
//     private mediconnectService: MediConnectService,
//     private router: Router
//   ) {}

//   ngOnInit(): void {
//     this.role = localStorage.getItem('role');
//     this.userId = Number(localStorage.getItem('user_id')) || 0;
//     this.doctorId = Number(localStorage.getItem('doctor_id')) || 0;
//     this.patientId = Number(localStorage.getItem('patient_id')) || 0;

//     if (this.role === 'DOCTOR' && this.doctorId > 0) {
//       this.loadDoctorData();
//     }
//   }

//   loadDoctorData(): void {
//     this.mediconnectService.getDoctorById(this.doctorId).subscribe({
//       next: (response) => {
//         this.doctorDetails = response;
//       },
//       error: () => {
//         // ✅ REQUIRED BY TEST
//         this.doctorDetails = undefined;
//       },
//     });

//     this.mediconnectService.getClinicsByDoctorId(this.doctorId).subscribe({
//       next: (response) => {
//         this.clinics = response || [];
//         if (this.clinics.length > 0 && this.clinics[0].clinicId) {
//           this.selectedClinicId = this.clinics[0].clinicId;
//           this.loadAppointments(this.selectedClinicId);
//         }
//       },
//       error: () => {},
//     });
//   }

//   loadAppointments(clinicId: number): void {
//     this.mediconnectService.getAppointmentsByClinic(clinicId).subscribe({
//       next: (response) => {
//         this.selectClinicAppointments = response || [];
//       },
//       error: () => {},
//     });
//   }

//   onClinicSelect(clinic: Clinic): void {
//     if (clinic.clinicId) {
//       this.selectedClinicId = clinic.clinicId;
//       this.loadAppointments(this.selectedClinicId);
//     }
//   }
// }


import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Appointment } from '../../models/Appointment';
import { Clinic } from '../../models/Clinic';
import { Doctor } from '../../models/Doctor';
import { Patient } from '../../models/Patient';
import { MediConnectService } from '../../services/mediconnect.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {

  doctorDetails: Doctor | undefined;
  patientDetails: Patient | undefined;

  doctors: Doctor[] = [];
  clinics: Clinic[] = [];
  appointments: Appointment[] = [];
  patients: Patient[] = [];

  role: string | null = null;
  userId = 0;
  doctorId = 0;
  patientId = 0;

  selectedClinicId?: number;
  selectClinicAppointments: Appointment[] = [];

  constructor(
    private mediconnectService: MediConnectService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.role = localStorage.getItem('role');
    this.userId = Number(localStorage.getItem('user_id')) || 0;
    this.doctorId = Number(localStorage.getItem('doctor_id')) || 0;
    this.patientId = Number(localStorage.getItem('patient_id')) || 0;

    if (this.role === 'DOCTOR') {
      this.loadDoctorData();
    } else {
      this.loadPatientData();
    }
  }

  /* ===================== DOCTOR DASHBOARD ===================== */

  loadDoctorData(): void {
    this.mediconnectService.getDoctorById(this.doctorId).subscribe({
      next: (response) => {
        this.doctorDetails = response;
      },
      error: () => {
        // ✅ Required for Day‑23 safety
        this.doctorDetails = undefined;
      }
    });

    this.mediconnectService.getClinicsByDoctorId(this.doctorId).subscribe({
      next: (response) => {
        this.clinics = response || [];
        if (this.clinics.length > 0 && this.clinics[0].clinicId) {
          this.selectedClinicId = this.clinics[0].clinicId;
          this.loadAppointments(this.selectedClinicId);
        }
      },
      error: () => {}
    });

    this.mediconnectService.getAllPatients().subscribe({
      next: (response) => {
        this.patients = response;
      },
      error: () => {}
    });
  }

  loadAppointments(clinicId: number): void {
    if (!clinicId) return;

    this.mediconnectService.getAppointmentsByClinic(clinicId).subscribe({
      next: (response) => {
        this.selectClinicAppointments = response || [];
      },
      error: () => {}
    });
  }

  onClinicSelect(clinic: Clinic): void {
    if (!clinic.clinicId) return;

    this.selectedClinicId = clinic.clinicId;
    this.loadAppointments(this.selectedClinicId);
  }

  /* ===================== PATIENT DASHBOARD ===================== */

  loadPatientData(): void {
    this.mediconnectService.getPatientById(this.patientId).subscribe({
      next: (response) => {
        this.patientDetails = response;
      },
      error: () => {
        // ✅ THIS FIX MAKES THE FAILED TEST PASS
        this.patientDetails = undefined;
      }
    });

    this.mediconnectService.getAppointmentsByPatient(this.patientId).subscribe({
      next: (response) => {
        this.appointments = response;
      },
      error: () => {}
    });

    this.mediconnectService.getAllClinics().subscribe({
      next: (response) => {
        this.clinics = response;
      },
      error: () => {}
    });

    this.mediconnectService.getAllDoctors().subscribe({
      next: (response) => {
        this.doctors = response;
      },
      error: () => {}
    });
  }

  navigateToEditPatient(): void {
    if (this.patientDetails?.patientId) {
      this.router.navigate(['mediconnect/patient/edit', this.patientDetails.patientId]);
    }
  }

  deletePatient(): void {
    if (confirm('Are you sure you want to delete this patient profile?')) {
      this.mediconnectService.deletePatient(this.patientId).subscribe({
        next: () => {
          this.router.navigate(['/']);
        },
        error: () => {}
      });
    }
  }
}