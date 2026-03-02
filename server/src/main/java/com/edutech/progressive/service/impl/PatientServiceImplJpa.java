package com.edutech.progressive.service.impl;

import com.edutech.progressive.entity.Patient;
import com.edutech.progressive.exception.PatientAlreadyExistsException;
import com.edutech.progressive.exception.PatientNotFoundException;
import com.edutech.progressive.repository.PatientRepository;
import com.edutech.progressive.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PatientServiceImplJpa implements PatientService {

    private final PatientRepository patientRepository;

    
    @Autowired
    public PatientServiceImplJpa(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(int patientId) {
        return patientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));
    }

    @Override
    public Integer addPatient(Patient patient) {
                if (patient.getEmail() != null) {
            patientRepository.findByEmail(patient.getEmail()).ifPresent(existing -> {
                throw new PatientAlreadyExistsException("Patient already exists with email: " + patient.getEmail());
            });
        }
        Patient saved = patientRepository.save(patient);
        return saved.getPatientId();
    }

    @Override
    public List<Patient> getAllPatientSortedByName() {
        List<Patient> list = patientRepository.findAll();
        list.sort(Comparator.comparing(p -> p.getFullName() == null ? "" : p.getFullName()));
        return list;
    }

    @Override
    public void updatePatient(Patient patient) {
        patientRepository.findById(patient.getPatientId()).ifPresent(existing -> {
            existing.setFullName(patient.getFullName());
            existing.setDateOfBirth(patient.getDateOfBirth());
            existing.setContactNumber(patient.getContactNumber());
            existing.setEmail(patient.getEmail());
            existing.setAddress(patient.getAddress());
            patientRepository.save(existing);
        });
    }

    @Override
    public void deletePatient(int patientId) {
        patientRepository.deleteById(patientId);
    }
}