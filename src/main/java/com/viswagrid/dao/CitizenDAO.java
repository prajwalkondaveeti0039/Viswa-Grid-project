package com.viswagrid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.viswagrid.model.Citizen;
import com.viswagrid.util.DBConnection;
import com.viswagrid.util.PasswordUtil;

public class CitizenDAO {

    // 1. Check whether email is already registered
    public boolean emailExists(String email) throws SQLException {

        String sql = "SELECT citizen_id FROM citizens WHERE email = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // 2. Register a new citizen
    public int registerCitizen(Citizen citizen) throws SQLException {

        String sql = "INSERT INTO citizens "
                + "(full_name, email, mobile, password_hash) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, citizen.getFullName());
            statement.setString(2, citizen.getEmail());
            statement.setString(3, citizen.getMobile());
            statement.setString(4, citizen.getPasswordHash());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet result = statement.getGeneratedKeys()) {

                    if (result.next()) {
                        int citizenId = result.getInt(1);
                        citizen.setCitizenId(citizenId);
                        return citizenId;
                    }
                }
            }
        }

        return -1;
    }

    // 3. Authenticate an existing citizen
    public Citizen validateCitizen(String email, String password)
            throws SQLException {

        String sql = "SELECT citizen_id, full_name, email, mobile, "
                   + "password_hash FROM citizens "
                   + "WHERE LOWER(email) = LOWER(?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email.trim());

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    String storedHash =
                            result.getString("password_hash");

                    if (PasswordUtil.checkPassword(
                            password, storedHash)) {

                        Citizen citizen = new Citizen();

                        citizen.setCitizenId(
                                result.getInt("citizen_id"));

                        citizen.setFullName(
                                result.getString("full_name"));

                        citizen.setEmail(
                                result.getString("email"));

                        citizen.setMobile(
                                result.getString("mobile"));

                        // Do not put the password hash into the session.
                        return citizen;
                    }
                }
            }
        }

        return null;
    }
}