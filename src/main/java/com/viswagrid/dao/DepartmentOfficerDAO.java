package com.viswagrid.dao;

import java.sql.*;
import java.util.*;
import com.viswagrid.util.DBConnection;
import com.viswagrid.util.PasswordUtil;
import com.viswagrid.model.Complaint;

public class DepartmentOfficerDAO {
    public Map<String,Object> validate(String username, String password) throws SQLException {
        String sql = "SELECT o.officer_id,o.username,o.full_name,o.department_id,d.department_name,o.password_hash "
                   + "FROM department_officers o JOIN departments d ON d.department_id=o.department_id "
                   + "WHERE o.username=? AND o.is_active=TRUE AND d.is_active=TRUE";
        try (Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs=ps.executeQuery()) {
                if (rs.next() && PasswordUtil.checkPassword(password, rs.getString("password_hash"))) {
                    Map<String,Object> user=new HashMap<>();
                    user.put("officerId",rs.getInt("officer_id")); user.put("username",rs.getString("username"));
                    user.put("fullName",rs.getString("full_name")); user.put("departmentId",rs.getInt("department_id"));
                    user.put("departmentName",rs.getString("department_name")); return user;
                }
            }
        }
        return null;
    }
    public boolean create(String username,String fullName,String password,int departmentId) throws SQLException {
        String sql="INSERT INTO department_officers(username,full_name,password_hash,department_id,is_active) SELECT ?,?,?,department_id,TRUE FROM departments WHERE department_id=? AND is_active=TRUE";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,username.trim()); ps.setString(2,fullName.trim());
            ps.setString(3,PasswordUtil.hashPassword(password)); ps.setInt(4,departmentId); return ps.executeUpdate()==1;
        }
    }
    public List<Map<String,Object>> getAll() throws SQLException {
        List<Map<String,Object>> list=new ArrayList<>();
        String sql="SELECT o.officer_id,o.username,o.full_name,o.is_active,d.department_name FROM department_officers o JOIN departments d ON d.department_id=o.department_id ORDER BY d.department_name,o.full_name";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql); ResultSet rs=ps.executeQuery()) {
            while(rs.next()){ Map<String,Object> m=new HashMap<>(); m.put("officerId",rs.getInt("officer_id")); m.put("username",rs.getString("username")); m.put("fullName",rs.getString("full_name")); m.put("active",rs.getBoolean("is_active")); m.put("departmentName",rs.getString("department_name")); list.add(m); }
        } return list;
    }
    public List<Complaint> getDepartmentComplaints(int departmentId) throws SQLException {
        List<Complaint> list=new ArrayList<>();
        String sql="SELECT c.*,d.department_name FROM complaints c JOIN departments d ON d.department_id=c.department_id WHERE c.department_id=? ORDER BY c.created_at DESC";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setInt(1,departmentId); try(ResultSet rs=ps.executeQuery()){ while(rs.next()) list.add(mapComplaint(rs)); }
        } return list;
    }
    public boolean updateComplaintStatus(long complaintId,int departmentId,String status) throws SQLException {
        if(!Arrays.asList("IN_PROGRESS","RESOLVED","REJECTED").contains(status)) throw new IllegalArgumentException("Invalid status");
        String sql="UPDATE complaints SET status=? WHERE complaint_id=? AND department_id=?";
        try(Connection c=DBConnection.getConnection(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,status); ps.setLong(2,complaintId); ps.setInt(3,departmentId); return ps.executeUpdate()==1;
        }
    }
    private Complaint mapComplaint(ResultSet rs) throws SQLException {
        Complaint x=new Complaint(); x.setComplaintId(rs.getLong("complaint_id")); x.setCitizenName(rs.getString("citizen_name"));
        x.setCategory(rs.getString("category")); x.setIssueTitle(rs.getString("issue_title")); x.setDescription(rs.getString("description"));
        x.setAddress(rs.getString("address")); x.setCity(rs.getString("city")); x.setPincode(rs.getString("pincode"));
        x.setPhotoPath(rs.getString("photo_path")); x.setStatus(rs.getString("status")); x.setDepartmentId(rs.getInt("department_id"));
        x.setDepartmentName(rs.getString("department_name")); return x;
    }
}
