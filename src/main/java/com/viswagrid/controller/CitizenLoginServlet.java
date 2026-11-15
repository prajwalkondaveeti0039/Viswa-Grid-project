package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.viswagrid.dao.CitizenDAO;
import com.viswagrid.model.Citizen;

@WebServlet("/citizen-login")
public class CitizenLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final CitizenDAO citizenDAO = new CitizenDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {

            request.setAttribute("error",
                    "Please enter both email and password.");

            request.getRequestDispatcher("/citizen-login.jsp")
                    .forward(request, response);

            return;
        }

        try {

            // Authenticate citizen using CitizenDAO
            Citizen citizen = citizenDAO.validateCitizen(
                    email.trim(), password);

            if (citizen != null) {

                // Create or reuse session
                HttpSession session = request.getSession(true);

                // Protect against session fixation
                request.changeSessionId();

                // Store authenticated citizen
                session.setAttribute("citizen", citizen);

                // Session expires after 30 minutes of inactivity
                session.setMaxInactiveInterval(30 * 60);

                // Redirect to citizen dashboard
                response.sendRedirect(
                        request.getContextPath()
                        + "/citizen-dashboard");

            } else {

                request.setAttribute("error",
                        "Invalid email address or password.");

                request.getRequestDispatcher("/citizen-login.jsp")
                        .forward(request, response);
            }

        } catch (SQLException e) {

            getServletContext().log(
                    "Citizen login database error.", e);

            request.setAttribute("error",
                    "Unable to process login. Please try again.");

            request.getRequestDispatcher("/citizen-login.jsp")
                    .forward(request, response);
        }
    }
}