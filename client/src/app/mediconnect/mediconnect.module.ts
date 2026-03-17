// import { NgModule } from "@angular/core";
// import { CommonModule } from "@angular/common";
// import { FormsModule, ReactiveFormsModule } from "@angular/forms";
// import { HttpClientModule } from "@angular/common/http";
// import { PatientCreateComponent } from "./components/patientcreate/patientcreate.component";
// import { ClinicCreateComponent } from "./components/cliniccreate/cliniccreate.component";
// import { DoctorCreateComponent } from "./components/doctorcreate/doctorcreate.component";
// import { AppointmentCreateComponent } from "./components/appointment/appointment.component";
// import { MediconnectRoutingModule } from "./mediconnect-routing.module";
// import { RouterModule } from "@angular/router";

// @NgModule({
//   declarations: [
//     PatientCreateComponent,
//     DoctorCreateComponent,
//     ClinicCreateComponent,
//     AppointmentCreateComponent
//   ],
//   imports: [
//     CommonModule,
//     ReactiveFormsModule,
//     HttpClientModule,
//     MediconnectRoutingModule,
//     RouterModule
//   ],
//   exports: [
//   ]
// })
// export class MediconnectModule {}
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
 
import { MediconnectRoutingModule } from './mediconnect-routing.module';
import { SharedModule } from '../shared/shared.module';
 
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PatientCreateComponent } from './components/patientcreate/patientcreate.component';
import { DoctorCreateComponent } from './components/doctorcreate/doctorcreate.component';
import { ClinicCreateComponent } from './components/cliniccreate/cliniccreate.component';
import { AppointmentCreateComponent } from './components/appointment/appointment.component';
import { PatientEditComponent } from './components/patientedit/patientedit.component';
import { ClinicEditComponent } from './components/clinicedit/clinicedit.component';
import { DoctorEditComponent } from './components/doctoredit/doctoredit.component';
 
@NgModule({
  declarations: [
    DashboardComponent,
    PatientCreateComponent,
    DoctorCreateComponent,
    ClinicCreateComponent,
    AppointmentCreateComponent,
    PatientEditComponent,
    ClinicEditComponent,
    DoctorEditComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    MediconnectRoutingModule,
    SharedModule    // << VERY IMPORTANT FOR DAY‑26
  ]
})
export class MediconnectModule {}