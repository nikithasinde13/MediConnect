package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Appointment;
import com.edutech.progressive.entity.Clinic;
import com.edutech.progressive.entity.Patient;
import com.edutech.progressive.repository.AppointmentRepository;
import com.edutech.progressive.repository.ClinicRepository;
import com.edutech.progressive.repository.PatientRepository;
import com.edutech.progressive.service.AppointmentService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private static final Set<String> ALLOWED_STATUS = Set.of("Scheduled", "Completed", "Canceled");

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ClinicRepository clinicRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientRepository patientRepository, ClinicRepository clinicRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Appointment getAppointmentById(Integer appointmentId) {
        Optional<Appointment> opt = appointmentRepository.findById(appointmentId);
        return opt.orElse(null);
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        attachRelationsIfIdsProvided(appointment);
        validateAppointment(appointment);
        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment updateAppointment(Integer appointmentId, Appointment update) {
        Appointment existing = appointmentRepository.findById(appointmentId).orElse(null);
        if (existing == null) {
            return null;
        }
        attachRelationsIfIdsProvided(update);
        if (update.getPatient() != null) existing.setPatient(update.getPatient());
        if (update.getClinic() != null) existing.setClinic(update.getClinic());
        if (update.getAppointmentDate() != null) existing.setAppointmentDate(update.getAppointmentDate());
        if (update.getStatus() != null) existing.setStatus(update.getStatus());
        if (update.getPurpose() != null) existing.setPurpose(update.getPurpose());
        validateAppointment(existing);
        return appointmentRepository.save(existing);
    }

    @Override
    public List<Appointment> getAppointmentByClinic(int clinicId) {
        return appointmentRepository.findByClinic_ClinicId(clinicId);
    }

    @Override
    public List<Appointment> getAppointmentByPatient(int patientId) {
        return appointmentRepository.findByPatient_PatientId(patientId);
    }

    @Override
    public List<Appointment> getAppointmentByStatus(String status) {
        return appointmentRepository.findByStatus(status);
    }

    private void attachRelationsIfIdsProvided(Appointment appointment) {
        if (appointment.getPatient() == null && appointment.getPatientId() != null) {
            Patient p = patientRepository.findById(appointment.getPatientId()).orElseThrow(() -> new IllegalArgumentException("Invalid patientId"));
            appointment.setPatient(p);
        }
        if (appointment.getClinic() == null && appointment.getClinicId() != null) {
            Clinic c = clinicRepository.findById(appointment.getClinicId()).orElseThrow(() -> new IllegalArgumentException("Invalid clinicId"));
            appointment.setClinic(c);
        }
    }

    private void validateAppointment(Appointment a) {
        if (a.getPatient() == null) throw new IllegalArgumentException("Patient is required");
        if (a.getClinic() == null) throw new IllegalArgumentException("Clinic is required");
        if (a.getAppointmentDate() == null) throw new IllegalArgumentException("Appointment date is required");
        if (a.getStatus() == null || !ALLOWED_STATUS.contains(a.getStatus())) throw new IllegalArgumentException("Invalid status");
    }
}