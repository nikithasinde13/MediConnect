package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Clinic;
import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.repository.ClinicRepository;
import com.edutech.progressive.repository.DoctorRepository;
import com.edutech.progressive.service.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicServiceImplJpa implements ClinicService {

    private final ClinicRepository clinicRepository;
    private final DoctorRepository doctorRepository; // may be null when constructed via single-arg constructor

    /**
     * ✅ Single-arg constructor (required by DayEightTest)
     * Tests instantiate: new ClinicServiceImplJpa(clinicRepository)
     */
    public ClinicServiceImplJpa(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
        this.doctorRepository = null; // safe default for tests
    }

    /**
     * ✅ Spring should use THIS constructor when wiring the bean in ApplicationContext.
     * Mark it with @Autowired to remove ambiguity when multiple constructors exist.
     */
    @Autowired
    public ClinicServiceImplJpa(ClinicRepository clinicRepository,
                                DoctorRepository doctorRepository) {
        this.clinicRepository = clinicRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    @Override
    public Clinic getClinicById(int clinicId) {
        return clinicRepository.findByClinicId(clinicId).orElse(null);
    }

    @Override
    public Integer addClinic(Clinic clinic) {
        // Attach doctor only if repository is available and doctorId provided
        if (doctorRepository != null && clinic.getDoctor() != null && clinic.getDoctor().getDoctorId() != 0) {
            Doctor managed = doctorRepository.findById(clinic.getDoctor().getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Doctor not found: " + clinic.getDoctor().getDoctorId()));
            clinic.setDoctor(managed);
        } else if (clinic.getDoctor() == null || clinic.getDoctor().getDoctorId() == 0) {
            clinic.setDoctor(null);
        }
        Clinic saved = clinicRepository.save(clinic);
        return saved.getClinicId();
    }

    @Override
    public void updateClinic(Clinic clinic) {
        clinicRepository.findById(clinic.getClinicId()).ifPresent(existing -> {
            existing.setClinicName(clinic.getClinicName());
            existing.setLocation(clinic.getLocation());
            existing.setContactNumber(clinic.getContactNumber());
            existing.setEstablishedYear(clinic.getEstablishedYear());

            if (doctorRepository != null && clinic.getDoctor() != null && clinic.getDoctor().getDoctorId() != 0) {
                Doctor managed = doctorRepository.findById(clinic.getDoctor().getDoctorId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Doctor not found: " + clinic.getDoctor().getDoctorId()));
                existing.setDoctor(managed);
            } else {
                if (clinic.getDoctor() == null || clinic.getDoctor().getDoctorId() == 0) {
                    existing.setDoctor(null);
                }
            }
            clinicRepository.save(existing);
        });
    }

    @Override
    public void deleteClinic(int clinicId) {
        clinicRepository.deleteById(clinicId);
    }

    @Override
    public List<Clinic> getAllClinicByLocation(String location) {
        return clinicRepository.findAllByLocation(location);
    }

    @Override
    public List<Clinic> getAllClinicByDoctorId(int doctorId) {
        return clinicRepository.findAllByDoctorId(doctorId);
    }
}