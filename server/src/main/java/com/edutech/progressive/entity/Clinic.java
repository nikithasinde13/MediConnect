package com.edutech.progressive.entity;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "clinic")
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinic_id")
    private int clinicId;

    @Column(name = "clinic_name", nullable = false, length = 255)
    private String clinicName;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "contact_number", length = 15)
    private String contactNumber;

    @Column(name = "established_year")
    private Integer establishedYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    @JsonIgnoreProperties({"clinics"})
    private Doctor doctor;

    public Clinic() {}

    public Clinic(int clinicId, String clinicName, String location, String contactNumber, Integer establishedYear, Doctor doctor) {
        this.clinicId = clinicId;
        this.clinicName = clinicName;
        this.location = location;
        this.contactNumber = contactNumber;
        this.establishedYear = establishedYear;
        this.doctor = doctor;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Integer getEstablishedYear() {
        return establishedYear;
    }

    public void setEstablishedYear(Integer establishedYear) {
        this.establishedYear = establishedYear;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
