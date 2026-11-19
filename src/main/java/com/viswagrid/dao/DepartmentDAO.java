package com.viswagrid.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.viswagrid.model.Department;
import com.viswagrid.util.DBConnection;

public class DepartmentDAO {
    public List<Department> getAllDepartments() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT department_id, department_name, description, is_active FROM departments ORDER BY department_name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public List<Department> getActiveDepartments() throws SQLException {
        List<Department> list = new ArrayList<>();
        String sql = "SELECT department_id, department_name, description, is_active FROM departments WHERE is_active = TRUE ORDER BY department_name";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public boolean addDepartment(String name, String description) throws SQLException {
        String sql = "INSERT INTO departments (department_name, description, is_active) VALUES (?, ?, TRUE)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setString(2, description == null ? "" : description.trim());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean updateDepartment(int id, String name, String description, boolean active) throws SQLException {
        String sql = "UPDATE departments SET department_name = ?, description = ?, is_active = ? WHERE department_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setString(2, description == null ? "" : description.trim());
            ps.setBoolean(3, active);
            ps.setInt(4, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Department map(ResultSet rs) throws SQLException {
        Department d = new Department();
        d.setDepartmentId(rs.getInt("department_id"));
        d.setDepartmentName(rs.getString("department_name"));
        d.setDescription(rs.getString("description"));
        d.setActive(rs.getBoolean("is_active"));
        return d;
    }
}
