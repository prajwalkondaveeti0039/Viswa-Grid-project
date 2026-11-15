package com.viswagrid.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                System.out.println("VISWA GRID DATABASE CONNECTED SUCCESSFULLY!");
                System.out.println("Database: " +
                        connection.getCatalog());
            }

        } catch (SQLException e) {
            System.out.println("DATABASE CONNECTION FAILED!");
            e.printStackTrace();
        }
    }
}