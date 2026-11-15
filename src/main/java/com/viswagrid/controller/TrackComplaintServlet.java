package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.viswagrid.dao.ComplaintDAO;
import com.viswagrid.model.Complaint;

@WebServlet("/track-complaint")
public class TrackComplaintServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String referenceId = request.getParameter("complaintId");

        if (referenceId == null || referenceId.trim().isEmpty()) {
            request.setAttribute("error",
                    "Please enter your complaint reference ID.");

            request.getRequestDispatcher("/track-complaint.jsp")
                    .forward(request, response);
            return;
        }

        referenceId = referenceId.trim().toUpperCase();

        if (!referenceId.matches("VG-\\d{5}")) {
            request.setAttribute("error",
                    "Invalid reference ID. Example: VG-00005");

            request.getRequestDispatcher("/track-complaint.jsp")
                    .forward(request, response);
            return;
        }

        long complaintId = Long.parseLong(
                referenceId.substring(3));

        try {
            Complaint complaint =
                    complaintDAO.getComplaintById(complaintId);

            if (complaint == null) {
                request.setAttribute("error",
                        "No complaint found with this reference ID.");
            } else {
                request.setAttribute("complaint", complaint);
                request.setAttribute("referenceId", referenceId);
            }

            request.getRequestDispatcher("/track-complaint.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            getServletContext().log(
                    "Error while tracking complaint.", e);

            request.setAttribute("error",
                    "Unable to retrieve complaint details. Please try again.");

            request.getRequestDispatcher("/track-complaint.jsp")
                    .forward(request, response);
        }
    }
}