package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.viswagrid.dao.ComplaintDAO;

@WebServlet("/update-complaint-status")
public class UpdateComplaintStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Check admin session
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(
                    request.getContextPath() + "/admin-login.jsp");
            return;
        }

        // 2. Read complaint ID and new status
        String idValue = request.getParameter("complaintId");
        String status = request.getParameter("status");

        // 3. Validate input
        if (idValue == null || status == null
                || !idValue.matches("\\d+")) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid complaint details.");
            return;
        }

        long complaintId;

        try {
            complaintId = Long.parseLong(idValue);

            if (complaintId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid complaint ID.");
            return;
        }

        try {

            // 4. Update status in MySQL
            boolean updated = complaintDAO.updateComplaintStatus(
                    complaintId, status);

            if (updated) {

                response.sendRedirect(
                        request.getContextPath()
                        + "/admin-dashboard?updated=1#complaints");

            } else {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Complaint not found.");
            }

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid complaint status.");

        } catch (SQLException e) {

            getServletContext().log(
                    "Error updating complaint status.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to update complaint status.");
        }
    }
}
