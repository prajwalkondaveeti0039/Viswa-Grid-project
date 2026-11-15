package com.viswagrid.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/viswa_grid";

    public static Connection getConnection() throws SQLException {

        String USER = System.getenv("VISWA_DB_USER");
        String PASSWORD = System.getenv("VISWA_DB_PASSWORD");

        if (USER == null || PASSWORD == null) {
            throw new SQLException(
                "Database credentials are not configured."
            );
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "MySQL JDBC Driver not found.", e
            );
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}