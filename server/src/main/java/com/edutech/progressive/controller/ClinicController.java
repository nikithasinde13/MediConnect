package com.edutech.progressive.controller;

import com.edutech.progressive.entity.Clinic;
import com.edutech.progressive.service.ClinicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinic")
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    // GET /clinic - 200 or 500
    @GetMapping
    public ResponseEntity<List<Clinic>> getAllClinics() {
        try {
            return ResponseEntity.ok(clinicService.getAllClinics());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /clinic/{clinicID} - 200 or 500
    @GetMapping("/{clinicID}")
    public ResponseEntity<Clinic> getClinicById(@PathVariable("clinicID") int clinicId) {
        try {
            Clinic clinic = clinicService.getClinicById(clinicId);
            // Spec allows only 200/500 – return 200 (even if null body)
            return ResponseEntity.ok(clinic);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST /clinic - 201 or 500
    @PostMapping
    public ResponseEntity<Integer> addClinic(@RequestBody Clinic clinic) {
        try {
            Integer id = clinicService.addClinic(clinic);
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT /clinic/{clinicID} - 200 or 500
    @PutMapping("/{clinicID}")
    public ResponseEntity<String> updateClinic(@PathVariable("clinicID") int clinicId,
                                               @RequestBody Clinic clinic) {
        try {
            clinic.setClinicId(clinicId);
            clinicService.updateClinic(clinic);
            return ResponseEntity.ok("Clinic updated");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating clinic");
        }
    }

    // DELETE /clinic/{clinicID} - 401 or 500 (per Day-8 spec)
    @DeleteMapping("/{clinicID}")
    public ResponseEntity<String> deleteClinic(@PathVariable("clinicID") int clinicId) {
        try {
            clinicService.deleteClinic(clinicId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Clinic deleted");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting clinic");
        }
    }

    // GET /clinic/location/{location} - 200 or 500
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Clinic>> getAllClinicByLocation(@PathVariable String location) {
        try {
            return ResponseEntity.ok(clinicService.getAllClinicByLocation(location));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET /clinic/doctor/{doctorId} - 200 or 500
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<Clinic>> getAllClinicByDoctorId(@PathVariable int doctorId) {
        try {
            return ResponseEntity.ok(clinicService.getAllClinicByDoctorId(doctorId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}