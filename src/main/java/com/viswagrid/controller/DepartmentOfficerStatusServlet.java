package com.viswagrid.controller;
import java.io.IOException; import java.sql.SQLException; import java.util.Map;
import jakarta.servlet.ServletException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*;
import com.viswagrid.dao.DepartmentOfficerDAO;
@WebServlet("/department-update-status")
public class DepartmentOfficerStatusServlet extends HttpServlet {
 private static final long serialVersionUID=1L; private final DepartmentOfficerDAO dao=new DepartmentOfficerDAO();
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  HttpSession s=req.getSession(false); if(s==null||s.getAttribute("departmentOfficer")==null){resp.sendRedirect(req.getContextPath()+"/department-login.jsp");return;}
  try { long id=Long.parseLong(req.getParameter("complaintId")); String status=req.getParameter("status"); Map<?,?> officer=(Map<?,?>)s.getAttribute("departmentOfficer");
   boolean ok=dao.updateComplaintStatus(id,(Integer)officer.get("departmentId"),status);
   if(!ok){resp.sendError(404,"Complaint not found in your department.");return;} resp.sendRedirect(req.getContextPath()+"/department-dashboard?updated=1");
  }catch(IllegalArgumentException e){resp.sendError(400,"Invalid complaint update.");}catch(SQLException e){getServletContext().log("Department status update failed",e);resp.sendError(500,"Unable to update complaint.");}
 }
}
