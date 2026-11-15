package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.viswagrid.dao.CitizenDAO;
import com.viswagrid.model.Citizen;
import com.viswagrid.util.PasswordUtil;

@WebServlet("/citizen-register")
public class CitizenRegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CitizenDAO citizenDAO = new CitizenDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        String confirmPassword =
                request.getParameter("confirmPassword");

        // 1. Validate required fields
        if (isBlank(fullName) || isBlank(email)
                || isBlank(mobile) || isBlank(password)
                || isBlank(confirmPassword)) {

            showError(request, response,
                    "Please complete all required fields.");
            return;
        }

        fullName = fullName.trim();
        email = email.trim().toLowerCase();
        mobile = mobile.trim();

        // 2. Validate name
        if (fullName.length() > 100) {
            showError(request, response,
                    "Name must not exceed 100 characters.");
            return;
        }

        if (!fullName.matches("[\\p{L} .'-]+")) {
            showError(request, response,
                    "Please enter a valid full name.");
            return;
        }

        // 3. Validate email
        if (email.length() > 150
                || !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {

            showError(request, response,
                    "Please enter a valid email address.");
            return;
        }

        // 4. Validate mobile number
        if (!mobile.matches("\\+?[0-9]{10,15}")) {
            showError(request, response,
                    "Please enter a valid mobile number.");
            return;
        }

        // 5. Validate password
        if (password.length() < 8 || password.length() > 72) {
            showError(request, response,
                    "Password must contain 8 to 72 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError(request, response,
                    "Passwords do not match.");
            return;
        }

        try {

            // 6. Check whether email already exists
            if (citizenDAO.emailExists(email)) {
                showError(request, response,
                        "This email is already registered.");
                return;
            }

            // 7. Hash password using BCrypt
            String passwordHash =
                    PasswordUtil.hashPassword(password);

            // 8. Create citizen object
            Citizen citizen = new Citizen(
                    fullName,
                    email,
                    mobile,
                    passwordHash
            );

            // 9. Save citizen in MySQL
            int citizenId =
                    citizenDAO.registerCitizen(citizen);

            if (citizenId > 0) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/citizen-login.jsp?registered=1"
                );

            } else {

                showError(request, response,
                        "Registration failed. Please try again.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {

            // Handles duplicate email submissions occurring together
            showError(request, response,
                    "This email is already registered.");

        } catch (SQLException e) {

            getServletContext().log(
                    "Citizen registration database error.", e);

            showError(request, response,
                    "Unable to complete registration. Please try again.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showError(HttpServletRequest request,
                           HttpServletResponse response,
                           String message)
            throws ServletException, IOException {

        request.setAttribute("error", message);

        request.getRequestDispatcher(
                "/citizen-register.jsp"
        ).forward(request, response);
    }
}