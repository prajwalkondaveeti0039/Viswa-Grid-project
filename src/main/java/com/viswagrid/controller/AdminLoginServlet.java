package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.viswagrid.dao.AdminDAO;
import com.viswagrid.model.Admin;

@WebServlet("/admin-login")
public class AdminLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null
                || username.trim().isEmpty()
                || password.isEmpty()) {

            request.setAttribute("error",
                    "Please enter both username and password.");

            request.getRequestDispatcher("/admin-login.jsp")
                    .forward(request, response);
            return;
        }

        try {
            Admin admin = adminDAO.validateAdmin(
                    username.trim(), password);

            if (admin != null) {

                HttpSession session = request.getSession(true);

                // Prevent session fixation
                request.changeSessionId();

                session.setAttribute("admin", admin);
                session.setMaxInactiveInterval(20 * 60);

                response.sendRedirect(
                        request.getContextPath() + "/admin-dashboard");

            } else {

                request.setAttribute("error",
                        "Invalid username or password.");

                request.getRequestDispatcher("/admin-login.jsp")
                        .forward(request, response);
            }

        } catch (SQLException e) {

            getServletContext().log(
                    "Admin login database error.", e);

            request.setAttribute("error",
                    "Unable to process login. Please try again.");

            request.getRequestDispatcher("/admin-login.jsp")
                    .forward(request, response);
        }
    }
}