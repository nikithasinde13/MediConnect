package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Doctor;
import com.edutech.progressive.exception.DoctorAlreadyExistsException;
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
    private final ClinicRepository clinicRepository; 

    
    public DoctorServiceImplJpa(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
        this.clinicRepository = null;
    }

    

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

        if (doctor.getEmail() != null) {
            doctorRepository.findByEmail(doctor.getEmail()).ifPresent(existing -> {
                throw new DoctorAlreadyExistsException("Doctor already exists with email: " + doctor.getEmail());
            });
        }
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
        if (clinicRepository != null) {
            clinicRepository.deleteByDoctorId(doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }
}