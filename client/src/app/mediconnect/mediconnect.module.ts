import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { PatientCreateComponent } from "./components/patientcreate/patientcreate.component";
import { ClinicCreateComponent } from "./components/cliniccreate/cliniccreate.component";
import { DoctorCreateComponent } from "./components/doctorcreate/doctorcreate.component";
import { AppointmentCreateComponent } from "./components/appointment/appointment.component";
import { MediconnectRoutingModule } from "./mediconnect-routing.module";
import { RouterModule } from "@angular/router";

@NgModule({
  declarations: [
    PatientCreateComponent,
    DoctorCreateComponent,
    ClinicCreateComponent,
    AppointmentCreateComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    HttpClientModule,
    MediconnectRoutingModule,
    RouterModule
  ],
  exports: [
  ]
})
export class MediconnectModule {}
