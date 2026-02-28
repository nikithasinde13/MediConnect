package com.edutech.progressive.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.edutech.progressive.config.DatabaseConnectionManager;
import com.edutech.progressive.entity.Clinic;
import com.edutech.progressive.entity.Doctor;

public class ClinicDAOImpl implements ClinicDAO {

    @Override
    public int addClinic(Clinic clinic) throws SQLException {
        String sql = "INSERT INTO clinic (clinic_name, location, doctor_id, contact_number, established_year) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet keys = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, clinic.getClinicName());
            ps.setString(2, clinic.getLocation());
            if (clinic.getDoctor() != null && clinic.getDoctor().getDoctorId() != 0) {
                ps.setInt(3, clinic.getDoctor().getDoctorId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, clinic.getContactNumber());
            ps.setInt(5, clinic.getEstablishedYear());
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
    public Clinic getClinicById(int clinicId) throws SQLException {
        String sql = "SELECT clinic_id, clinic_name, location, doctor_id, contact_number, established_year FROM clinic WHERE clinic_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Clinic c = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clinicId);
            rs = ps.executeQuery();
            if (rs.next()) {
                c = mapRow(rs);
            }
            return c;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void updateClinic(Clinic clinic) throws SQLException {
        String sql = "UPDATE clinic SET clinic_name = ?, location = ?, doctor_id = ?, contact_number = ?, established_year = ? WHERE clinic_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, clinic.getClinicName());
            ps.setString(2, clinic.getLocation());
            if (clinic.getDoctor() != null && clinic.getDoctor().getDoctorId() != 0) {
                ps.setInt(3, clinic.getDoctor().getDoctorId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, clinic.getContactNumber());
            ps.setInt(5, clinic.getEstablishedYear());
            ps.setInt(6, clinic.getClinicId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void deleteClinic(int clinicId) throws SQLException {
        String sql = "DELETE FROM clinic WHERE clinic_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, clinicId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    @Override
    public List<Clinic> getAllClinics() throws SQLException {
        String sql = "SELECT clinic_id, clinic_name, location, doctor_id, contact_number, established_year FROM clinic";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Clinic> list = new ArrayList<>();
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

    private Clinic mapRow(ResultSet rs) throws SQLException {
        Clinic c = new Clinic();
        c.setClinicId(rs.getInt("clinic_id"));
        c.setClinicName(rs.getString("clinic_name"));
        c.setLocation(rs.getString("location"));
        c.setContactNumber(rs.getString("contact_number"));
        c.setEstablishedYear(rs.getInt("established_year"));

        int doctorId = rs.getInt("doctor_id");
        if (!rs.wasNull()) {
            Doctor d = new Doctor();
            d.setDoctorId(doctorId);
            c.setDoctor(d);
        } else {
            c.setDoctor(null);
        }
        return c;
        }
}