package com.viswagrid.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import com.viswagrid.model.Complaint;
import com.viswagrid.util.DBConnection;

public class ComplaintDAO {

    // 1. Add a new complaint
    public long addComplaint(Complaint complaint) throws SQLException {

        String sql = "INSERT INTO complaints "
                + "(citizen_name, category, issue_title, description, "
                + "address, city, pincode, photo_path, status, "
                + "latitude, longitude, citizen_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, complaint.getCitizenName());
            statement.setString(2, complaint.getCategory());
            statement.setString(3, complaint.getIssueTitle());
            statement.setString(4, complaint.getDescription());
            statement.setString(5, complaint.getAddress());
            statement.setString(6, complaint.getCity());
            statement.setString(7, complaint.getPincode());
            statement.setString(8, complaint.getPhotoPath());
            statement.setString(9, "SUBMITTED");

            if (complaint.getLatitude() != null) {
                statement.setDouble(10, complaint.getLatitude());
            } else {
                statement.setNull(10, Types.DOUBLE);
            }

            if (complaint.getLongitude() != null) {
                statement.setDouble(11, complaint.getLongitude());
            } else {
                statement.setNull(11, Types.DOUBLE);
            }

            if (complaint.getCitizenId() != null) {
                statement.setInt(12, complaint.getCitizenId());
            } else {
                statement.setNull(12, Types.INTEGER);
            }

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet result = statement.getGeneratedKeys()) {
                    if (result.next()) {
                        return result.getLong(1);
                    }
                }
            }
        }

        return -1;
    }

    // 2. Get a complaint using its ID
    public Complaint getComplaintById(long complaintId)
            throws SQLException {

        String sql = "SELECT c.*, d.department_name FROM complaints c LEFT JOIN departments d ON c.department_id = d.department_id WHERE c.complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, complaintId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapComplaint(result);
                }
            }
        }

        return null;
    }

    // 3. Get all complaints for the admin dashboard
    public List<Complaint> getAllComplaints() throws SQLException {

        List<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT c.*, d.department_name FROM complaints c LEFT JOIN departments d ON c.department_id = d.department_id ORDER BY c.created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                complaints.add(mapComplaint(result));
            }
        }

        return complaints;
    }

    // 4. Get complaints belonging to one citizen
    public List<Complaint> getComplaintsByCitizenId(int citizenId)
            throws SQLException {

        List<Complaint> complaints = new ArrayList<>();

        String sql = "SELECT c.*, d.department_name FROM complaints c "
                   + "LEFT JOIN departments d ON c.department_id = d.department_id "
                   + "WHERE c.citizen_id = ? ORDER BY c.created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, citizenId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    complaints.add(mapComplaint(result));
                }
            }
        }

        return complaints;
    }

    // 5. Update complaint status
    public boolean updateComplaintStatus(long complaintId, String status)
            throws SQLException {

        if (!"SUBMITTED".equals(status)
                && !"ASSIGNED".equals(status)
                && !"IN_PROGRESS".equals(status)
                && !"RESOLVED".equals(status)
                && !"REJECTED".equals(status)) {

            throw new IllegalArgumentException(
                    "Invalid complaint status.");
        }

        String sql = "UPDATE complaints SET status = ? "
                   + "WHERE complaint_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, status);
            statement.setLong(2, complaintId);

            return statement.executeUpdate() > 0;
        }
    }


    // Assign a complaint to an active department.
    public boolean assignComplaintToDepartment(long complaintId, int departmentId)
            throws SQLException {
        String sql = "UPDATE complaints c JOIN departments d "
                   + "ON d.department_id = ? AND d.is_active = TRUE "
                   + "SET c.department_id = ?, c.status = 'ASSIGNED' "
                   + "WHERE c.complaint_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, departmentId);
            statement.setInt(2, departmentId);
            statement.setLong(3, complaintId);
            return statement.executeUpdate() > 0;
        }
    }

    // 7. Convert database record into Complaint object
    private Complaint mapComplaint(ResultSet result)
            throws SQLException {

        Complaint complaint = new Complaint();

        complaint.setComplaintId(
                result.getLong("complaint_id"));

        complaint.setCitizenName(
                result.getString("citizen_name"));

        complaint.setCategory(
                result.getString("category"));

        complaint.setIssueTitle(
                result.getString("issue_title"));

        complaint.setDescription(
                result.getString("description"));

        complaint.setAddress(
                result.getString("address"));

        complaint.setCity(
                result.getString("city"));

        complaint.setPincode(
                result.getString("pincode"));

        complaint.setPhotoPath(
                result.getString("photo_path"));

        complaint.setStatus(
                result.getString("status"));

        int departmentId = result.getInt("department_id");
        complaint.setDepartmentId(result.wasNull() ? null : departmentId);
        complaint.setDepartmentName(result.getString("department_name"));

        double latitude = result.getDouble("latitude");
        complaint.setLatitude(
                result.wasNull() ? null : latitude);

        double longitude = result.getDouble("longitude");
        complaint.setLongitude(
                result.wasNull() ? null : longitude);

        int citizenId = result.getInt("citizen_id");
        complaint.setCitizenId(
                result.wasNull() ? null : citizenId);

        return complaint;
    }
}