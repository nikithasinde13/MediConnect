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
    doctorDetails: any;
    clinics: Clinic[] = [];
    appointments: Appointment[] = [];
    patients: Patient[] = [];

    role: string | null;
    userId: number;
    doctorId: number;
    patientId: number;

    selectedClinicId: number | undefined;
    selectClinicAppointments: Appointment[] = [];

    constructor(private mediconnectService: MediConnectService, private router: Router) { }

    ngOnInit(): void {
        this.role = localStorage.getItem("role");
        this.userId = Number(localStorage.getItem("user_id"));
        this.doctorId = Number(localStorage.getItem("doctor_id"));
        this.patientId = Number(localStorage.getItem("patient_id"));
        if (this.role === 'DOCTOR') {
            console.log('loadDoctorData');
            this.loadDoctorData();
        }
    }

    loadDoctorData(): void {
        this.mediconnectService.getDoctorById(this.doctorId).subscribe({
            next: (response) => {
                this.doctorDetails = response;
            },
            error: (error) => console.log('Error loading loggedIn doctor details', error)
        });

        this.mediconnectService.getClinicsByDoctorId(this.doctorId).subscribe({
            next: (response) => {
                this.clinics = response;
                if (this.clinics.length > 0) {
                    this.selectedClinicId = this.clinics[0].clinicId;
                    this.loadAppointments(this.selectedClinicId);
                }
            },
            error: (error) => console.log('Error loading clinics', error)
        });

        this.mediconnectService.getAllPatients().subscribe({
            next: (response) => {
                this.patients = response;
            },
            error: (error) => console.log('Error loading all patients.', error)
        });
    }

    loadAppointments(clinicId: number): void {
        this.mediconnectService.getAppointmentsByClinic(clinicId).subscribe({
            next: (response) => {
                this.selectClinicAppointments = response;
            },
            error: (error) => console.log('Error loading appointments', error),
        });
    }

    onClinicSelect(clinic: Clinic): void {
        this.selectedClinicId = clinic.clinicId;
        this.loadAppointments(this.selectedClinicId);
    }
}
