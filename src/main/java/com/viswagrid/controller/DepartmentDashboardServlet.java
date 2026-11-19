package com.viswagrid.controller;
import java.io.IOException; import java.sql.SQLException; import java.util.Map;
import jakarta.servlet.ServletException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*;
import com.viswagrid.dao.DepartmentOfficerDAO;
@WebServlet("/department-dashboard")
public class DepartmentDashboardServlet extends HttpServlet {
 private static final long serialVersionUID=1L; private final DepartmentOfficerDAO dao=new DepartmentOfficerDAO();
 protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false); if(s==null||s.getAttribute("departmentOfficer")==null){resp.sendRedirect(req.getContextPath()+"/department-login.jsp");return;}
  try { Map<?,?> officer=(Map<?,?>)s.getAttribute("departmentOfficer"); int dept=(Integer)officer.get("departmentId");
   req.setAttribute("complaints",dao.getDepartmentComplaints(dept)); req.getRequestDispatcher("/department-dashboard.jsp").forward(req,resp);
  }catch(SQLException e){getServletContext().log("Unable to load department dashboard",e);resp.sendError(500,"Unable to load department complaints.");}
 }
}
