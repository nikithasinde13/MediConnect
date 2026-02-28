package com.edutech.progressive.repository;

import com.edutech.progressive.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    Doctor findByDoctorId(int doctorId);
}