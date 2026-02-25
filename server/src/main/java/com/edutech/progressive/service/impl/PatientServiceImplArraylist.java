package com.edutech.progressive.service.impl;
 
import com.edutech.progressive.entity.Patient;

import com.edutech.progressive.service.PatientService;
 
import java.util.ArrayList;

import java.util.Comparator;

import java.util.List;
 
public class PatientServiceImplArraylist implements PatientService {
 
    // Day 2/3: In-memory store

    private static final List<Patient> patientList = new ArrayList<>();
 
    // Optional helper to clear list (useful for tests)

    public void emptyArrayList() {

        patientList.clear();

    }
 
    @Override

    public List<Patient> getAllPatients() throws Exception {

        return new ArrayList<>(patientList); // return a copy

    }
 
    @Override

    public Integer addPatient(Patient patient) throws Exception {

        patientList.add(patient);

        return patient.getPatientId();

    }
 
    @Override

    public List<Patient> getAllPatientSortedByName() throws Exception {

        List<Patient> copy = new ArrayList<>(patientList);

        copy.sort(Comparator.comparing(

                p -> p.getFullName() == null ? "" : p.getFullName(),

                String.CASE_INSENSITIVE_ORDER

        ));

        return copy;

    }
 
    @Override

    public void updatePatient(Patient patient) throws Exception {

        if (patient == null) return;

        for (int i = 0; i < patientList.size(); i++) {

            if (patientList.get(i).getPatientId() == patient.getPatientId()) {

                Patient p = patientList.get(i);

                p.setFullName(patient.getFullName());

                p.setDateOfBirth(patient.getDateOfBirth());

                p.setContactNumber(patient.getContactNumber());

                p.setEmail(patient.getEmail());

                p.setAddress(patient.getAddress());

                return;

            }

        }

    }
 
    @Override

    public void deletePatient(int patientId) throws Exception {

        patientList.removeIf(p -> p.getPatientId() == patientId);

    }
 
    @Override

    public Patient getPatientById(int patientId) throws Exception {

        for (Patient p : patientList) {

            if (p.getPatientId() == patientId) {

                return p;

            }

        }

        return null;

    }

}
 