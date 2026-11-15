package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.viswagrid.dao.ComplaintDAO;
import com.viswagrid.model.Citizen;
import com.viswagrid.model.Complaint;

@WebServlet("/citizen-dashboard")
public class CitizenDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Check citizen session
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("citizen") == null) {

            response.sendRedirect(
                request.getContextPath() + "/citizen-login.jsp"
            );
            return;
        }

        Citizen citizen =
                (Citizen) session.getAttribute("citizen");

        try {

            // Retrieve only this citizen's complaints
            List<Complaint> complaints =
                    complaintDAO.getComplaintsByCitizenId(
                            citizen.getCitizenId()
                    );

            request.setAttribute("citizen", citizen);
            request.setAttribute("complaints", complaints);

            request.getRequestDispatcher(
                    "/citizen-dashboard.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            getServletContext().log(
                    "Unable to load citizen dashboard.", e);

            request.setAttribute(
                    "error",
                    "Unable to load your complaints. Please try again."
            );

            request.setAttribute("citizen", citizen);

            request.getRequestDispatcher(
                    "/citizen-dashboard.jsp"
            ).forward(request, response);
        }
    }
}