package com.viswagrid.controller;
import java.io.IOException; import jakarta.servlet.annotation.WebServlet; import jakarta.servlet.http.*;
@WebServlet("/department-logout")
public class DepartmentOfficerLogoutServlet extends HttpServlet {
 private static final long serialVersionUID=1L;
 protected void doPost(HttpServletRequest req,HttpServletResponse resp)throws IOException{HttpSession s=req.getSession(false);if(s!=null)s.invalidate();resp.sendRedirect(req.getContextPath()+"/index.html");}
}
