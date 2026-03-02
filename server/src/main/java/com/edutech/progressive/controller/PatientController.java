package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Patient;
import com.edutech.progressive.service.impl.PatientServiceImplJpa;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientServiceImplJpa service;

    public PatientController(PatientServiceImplJpa service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            return ResponseEntity.ok(service.getAllPatients());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{patientID}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("patientID") Long patientId) {
        try {
            Patient p = service.getPatientById(patientId.intValue());
            return ResponseEntity.ok(p); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Integer> addPatient(@RequestBody Patient patient) {
        try {
            Integer id = service.addPatient(patient);
            return ResponseEntity.ok(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{patientID}")
    public ResponseEntity<String> updatePatient(@PathVariable("patientID") Long patientId,
                                                @RequestBody Patient patient) {
        try {
            patient.setPatientId(patientId.intValue());
            service.updatePatient(patient);
            return ResponseEntity.ok("Patient updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating patient");
        }
    }

   
    @DeleteMapping("/{patientID}")
    public ResponseEntity<String> deletePatient(@PathVariable("patientID") Long patientId) {
        try {
            service.deletePatient(patientId.intValue());
            return ResponseEntity.ok("Patient deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting patient");
        }
    }

    @GetMapping("/fromArrayList/sorted")
    public ResponseEntity<List<Patient>> getAllPatientSortedByName() {
        try {
            return ResponseEntity.ok(service.getAllPatientSortedByName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}