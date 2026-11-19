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
import com.viswagrid.dao.DepartmentDAO;
import com.viswagrid.model.Complaint;

@WebServlet("/admin-dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(
                request.getContextPath() + "/admin-login.jsp");
            return;
        }

        try {
            List<Complaint> complaints =
                    complaintDAO.getAllComplaints();

            request.setAttribute("complaints", complaints);
            request.setAttribute("departments", departmentDAO.getActiveDepartments());

            request.getRequestDispatcher("/admin-dashboard.jsp")
                    .forward(request, response);

        } catch (SQLException e) {

            getServletContext().log(
                    "Unable to load admin dashboard complaints.", e);

            request.setAttribute("error",
                    "Unable to load complaints. Please try again.");

            request.getRequestDispatcher("/admin-dashboard.jsp")
                    .forward(request, response);
        }
    }
}