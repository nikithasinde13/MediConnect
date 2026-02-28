package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.repository.ClinicRepository;
import com.edutech.progressive.repository.DoctorRepository;
import com.edutech.progressive.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DoctorServiceImplJpa implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final ClinicRepository clinicRepository; // may be null when constructed via single-arg constructor

    /**
     * ✅ Single-arg constructor (required by DayEightTest)
     * Tests instantiate: new DoctorServiceImplJpa(doctorRepository)
     */
    public DoctorServiceImplJpa(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = null; // safe default for tests
    }

    /**
     * ✅ Spring should use THIS constructor when wiring the bean in ApplicationContext.
     * Mark it with @Autowired to remove ambiguity when multiple constructors exist.
     */
    @Autowired
    public DoctorServiceImplJpa(DoctorRepository doctorRepository,
                                ClinicRepository clinicRepository) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getDoctorById(int doctorId) {
        return doctorRepository.findByDoctorId(doctorId).orElse(null);
    }

    @Override
    public Integer addDoctor(Doctor doctor) {
        Doctor saved = doctorRepository.save(doctor);
        return saved.getDoctorId();
    }

    @Override
    public List<Doctor> getDoctorSortedByExperience() {
        List<Doctor> list = doctorRepository.findAll();
        list.sort(Comparator.comparing(d -> d.getYearsOfExperience() == null ? 0 : d.getYearsOfExperience()));
        return list;
    }

    @Override
    public void updateDoctor(Doctor doctor) {
        doctorRepository.findById(doctor.getDoctorId()).ifPresent(existing -> {
            existing.setFullName(doctor.getFullName());
            existing.setSpecialty(doctor.getSpecialty());
            existing.setContactNumber(doctor.getContactNumber());
            existing.setEmail(doctor.getEmail());
            existing.setYearsOfExperience(doctor.getYearsOfExperience());
            doctorRepository.save(existing);
        });
    }

    @Override
    public void deleteDoctor(int doctorId) {
        // Day-8: if we have the ClinicRepository (in Spring context), delete child clinics first
        if (clinicRepository != null) {
            clinicRepository.deleteByDoctorId(doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }
}