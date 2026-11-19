package com.viswagrid.controller;
import java.io.IOException; import java.sql.SQLException;
import jakarta.servlet.ServletException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*;
import com.viswagrid.dao.DepartmentDAO; import com.viswagrid.dao.DepartmentOfficerDAO;
@WebServlet("/admin-officers")
public class AdminOfficerManagementServlet extends HttpServlet {
 private static final long serialVersionUID=1L; private final DepartmentDAO departments=new DepartmentDAO(); private final DepartmentOfficerDAO officers=new DepartmentOfficerDAO();
 protected void doGet(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  if(!admin(req)){resp.sendRedirect(req.getContextPath()+"/admin-login.jsp");return;}
  try{req.setAttribute("departments",departments.getActiveDepartments());req.setAttribute("officers",officers.getAll());req.getRequestDispatcher("/officer-management.jsp").forward(req,resp);}
  catch(SQLException e){getServletContext().log("Unable to load officers",e);resp.sendError(500,"Unable to load officer management.");}
 }
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws ServletException,IOException{
  if(!admin(req)){resp.sendRedirect(req.getContextPath()+"/admin-login.jsp");return;} req.setCharacterEncoding("UTF-8");
  String username=req.getParameter("username"),fullName=req.getParameter("fullName"),password=req.getParameter("password");
  try{int dept=Integer.parseInt(req.getParameter("departmentId")); if(username==null||!username.matches("[A-Za-z0-9_.-]{3,50}")||fullName==null||fullName.isBlank()||password==null||password.length()<8||password.length()>72||dept<=0){resp.sendError(400,"Enter a valid username, name, department and password (8-72 characters).");return;}
   officers.create(username,fullName,password,dept);resp.sendRedirect(req.getContextPath()+"/admin-officers?created=1");
  }catch(NumberFormatException e){resp.sendError(400,"Choose a valid department.");}catch(SQLException e){getServletContext().log("Unable to create officer",e);resp.sendError(500,"Could not create officer. The username may already exist.");}
 }
 private boolean admin(HttpServletRequest r){HttpSession s=r.getSession(false);return s!=null&&s.getAttribute("admin")!=null;}
}
