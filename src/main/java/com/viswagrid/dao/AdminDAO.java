package com.viswagrid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.viswagrid.model.Admin;
import com.viswagrid.util.DBConnection;
import com.viswagrid.util.PasswordUtil;

public class AdminDAO {

    public Admin validateAdmin(String username, String password)
            throws SQLException {

        String sql = "SELECT * FROM admins WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    String storedHash =
                            result.getString("password_hash");

                    if (PasswordUtil.checkPassword(
                            password, storedHash)) {

                        Admin admin = new Admin();

                        admin.setAdminId(
                                result.getInt("admin_id"));

                        admin.setUsername(
                                result.getString("username"));

                        admin.setFullName(
                                result.getString("full_name"));

                        return admin;
                    }
                }
            }
        }

        return null;
    }
}