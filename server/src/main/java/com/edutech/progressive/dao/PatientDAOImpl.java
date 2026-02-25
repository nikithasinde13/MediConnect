package com.edutech.progressive.dao;
 
import java.sql.*;

import java.util.ArrayList;

import java.util.List;
 
import com.edutech.progressive.config.DatabaseConnectionManager;

import com.edutech.progressive.entity.Patient;
 
public class PatientDAOImpl implements PatientDAO {
 
    @Override

    public int addPatient(Patient patient) throws SQLException {

        String sql = "INSERT INTO patient (full_name, date_of_birth, contact_number, email, address) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;

        PreparedStatement ps = null;

        ResultSet keys = null;

        try {

            conn = DatabaseConnectionManager.getConnection();

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, patient.getFullName());

            if (patient.getDateOfBirth() != null) {

                ps.setDate(2, new java.sql.Date(patient.getDateOfBirth().getTime()));

            } else {

                ps.setNull(2, Types.DATE);

            }

            ps.setString(3, patient.getContactNumber());

            ps.setString(4, patient.getEmail());

            ps.setString(5, patient.getAddress());

            ps.executeUpdate();

            keys = ps.getGeneratedKeys();

            if (keys.next()) {

                return keys.getInt(1);

            }

            return -1;

        } catch (SQLException e) {

            throw e;

        } finally {

            if (keys != null) try { keys.close(); } catch (SQLException ignored) {}

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}

            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}

        }

    }
 
    @Override

    public Patient getPatientById(int patientId) throws SQLException {

        String sql = "SELECT patient_id, full_name, date_of_birth, contact_number, email, address FROM patient WHERE patient_id = ?";

        Connection conn = null;

        PreparedStatement ps = null;

        ResultSet rs = null;

        Patient p = null;

        try {

            conn = DatabaseConnectionManager.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, patientId);

            rs = ps.executeQuery();

            if (rs.next()) {

                p = mapRow(rs);

            }

            return p;

        } catch (SQLException e) {

            throw e;

        } finally {

            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}

            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}

        }

    }
 
    @Override

    public void updatePatient(Patient patient) throws SQLException {

        String sql = "UPDATE patient SET full_name = ?, date_of_birth = ?, contact_number = ?, email = ?, address = ? WHERE patient_id = ?";

        Connection conn = null;

        PreparedStatement ps = null;

        try {

            conn = DatabaseConnectionManager.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setString(1, patient.getFullName());

            if (patient.getDateOfBirth() != null) {

                ps.setDate(2, new java.sql.Date(patient.getDateOfBirth().getTime()));

            } else {

                ps.setNull(2, Types.DATE);

            }

            ps.setString(3, patient.getContactNumber());

            ps.setString(4, patient.getEmail());

            ps.setString(5, patient.getAddress());

            ps.setInt(6, patient.getPatientId());

            ps.executeUpdate();

        } catch (SQLException e) {

            throw e;

        } finally {

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}

            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}

        }

    }
 
    @Override

    public void deletePatient(int patientId) throws SQLException {

        String sql = "DELETE FROM patient WHERE patient_id = ?";

        Connection conn = null;

        PreparedStatement ps = null;

        try {

            conn = DatabaseConnectionManager.getConnection();

            ps = conn.prepareStatement(sql);

            ps.setInt(1, patientId);

            ps.executeUpdate();

        } catch (SQLException e) {

            throw e;

        } finally {

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}

            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}

        }

    }
 
    @Override

    public List<Patient> getAllPatients() throws SQLException {

        String sql = "SELECT patient_id, full_name, date_of_birth, contact_number, email, address FROM patient";

        Connection conn = null;

        PreparedStatement ps = null;

        ResultSet rs = null;

        List<Patient> list = new ArrayList<>();

        try {

            conn = DatabaseConnectionManager.getConnection();

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {

                list.add(mapRow(rs));

            }

            return list;

        } catch (SQLException e) {

            throw e;

        } finally {

            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}

            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}

            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}

        }

    }
 
    private Patient mapRow(ResultSet rs) throws SQLException {

        Patient p = new Patient();

        p.setPatientId(rs.getInt("patient_id"));

        p.setFullName(rs.getString("full_name"));

        Date dob = rs.getDate("date_of_birth");

        if (dob != null) {

            p.setDateOfBirth(new java.util.Date(dob.getTime()));

        } else {

            p.setDateOfBirth(null);

        }

        p.setContactNumber(rs.getString("contact_number"));

        p.setEmail(rs.getString("email"));

        p.setAddress(rs.getString("address"));

        return p;

    }

}

 