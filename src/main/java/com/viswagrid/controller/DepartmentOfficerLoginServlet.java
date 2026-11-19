package com.viswagrid.controller;
import java.io.IOException; import java.sql.SQLException;
import jakarta.servlet.ServletException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*;
import com.viswagrid.dao.DepartmentOfficerDAO;
@WebServlet("/department-login")
public class DepartmentOfficerLoginServlet extends HttpServlet {
 private static final long serialVersionUID=1L; private final DepartmentOfficerDAO dao=new DepartmentOfficerDAO();
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  req.setCharacterEncoding("UTF-8"); String username=req.getParameter("username"),password=req.getParameter("password");
  try { var officer=dao.validate(username,password); if(officer==null){resp.sendRedirect(req.getContextPath()+"/department-login.jsp?error=1");return;}
   HttpSession old=req.getSession(false); if(old!=null) old.invalidate(); HttpSession s=req.getSession(true); s.setAttribute("departmentOfficer",officer);
   resp.sendRedirect(req.getContextPath()+"/department-dashboard");
  }catch(SQLException e){getServletContext().log("Department officer login failed",e);resp.sendError(500,"Unable to sign in.");}
 }
}
