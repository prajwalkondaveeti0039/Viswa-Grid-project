package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import com.viswagrid.dao.ComplaintDAO;

@WebServlet("/assign-complaint-department")
public class AssignComplaintDepartmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ComplaintDAO dao = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("admin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        try {
            long complaintId = Long.parseLong(request.getParameter("complaintId"));
            int departmentId = Integer.parseInt(request.getParameter("departmentId"));
            if (complaintId <= 0 || departmentId <= 0 ||
                    !dao.assignComplaintToDepartment(complaintId, departmentId)) {
                response.sendError(400, "Could not assign complaint. Check that the complaint and active department exist.");
                return;
            }
            response.sendRedirect(request.getContextPath() + "/admin-dashboard?assigned=1#complaints");
        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid complaint or department ID.");
        } catch (SQLException e) {
            getServletContext().log("Unable to assign complaint.", e);
            response.sendError(500, "Unable to assign complaint. Check the department database setup.");
        }
    }
}
