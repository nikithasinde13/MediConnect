package com.edutech.progressive.service;

import com.edutech.progressive.entity.Appointment;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAllAppointments();
    Appointment createAppointment(Appointment appointment);
    Appointment updateAppointment(Integer appointmentId, Appointment appointment);
    Appointment getAppointmentById(Integer appointmentId);
    List<Appointment> getAppointmentByClinic(int clinicId);
    List<Appointment> getAppointmentByPatient(int patientId);
    List<Appointment> getAppointmentByStatus(String status);
}