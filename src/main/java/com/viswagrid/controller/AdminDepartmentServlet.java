package com.viswagrid.controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.viswagrid.dao.DepartmentDAO;

@WebServlet("/admin-departments")
public class AdminDepartmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final DepartmentDAO dao = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        try {
            request.setAttribute("departments", dao.getAllDepartments());
            request.getRequestDispatcher("/department-management.jsp").forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("Unable to load departments.", e);
            response.sendError(500, "Unable to load departments. Check the database migration.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("departmentName");
        String description = request.getParameter("description");
        String idValue = request.getParameter("departmentId");
        String activeValue = request.getParameter("active");

        try {
            if (idValue == null || idValue.isBlank()) {
                if (name == null || name.isBlank() || name.trim().length() > 100) {
                    response.sendError(400, "Department name is required (maximum 100 characters).");
                    return;
                }
                dao.addDepartment(name, description);
            } else {
                int id = Integer.parseInt(idValue);
                if (id <= 0 || name == null || name.isBlank() || name.trim().length() > 100) {
                    response.sendError(400, "Invalid department details.");
                    return;
                }
                dao.updateDepartment(id, name, description, "true".equals(activeValue));
            }
            response.sendRedirect(request.getContextPath() + "/admin-departments?saved=1");
        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid department ID.");
        } catch (SQLException e) {
            getServletContext().log("Unable to save department.", e);
            response.sendError(500, "Unable to save department. The name may already exist.");
        }
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("admin") != null;
    }
}
