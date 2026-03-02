package com.edutech.progressive.repository;

import com.edutech.progressive.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    Optional<Patient> findByPatientId(Integer patientId);

    // Day-9: needed for duplicate email checks
    Optional<Patient> findByEmail(String email);
}