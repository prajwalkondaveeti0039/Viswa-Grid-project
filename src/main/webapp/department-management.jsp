<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.viswagrid.model.Department" %>
<%@ page import="com.viswagrid.model.Admin" %>
<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace(String.valueOf((char)34), "&quot;")
                    .replace("'", "&#39;");
    }
%>
<%
    Admin admin = (Admin) session.getAttribute("admin");
    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
        return;
    }
    List<Department> departments = (List<Department>) request.getAttribute("departments");
    if (departments == null) departments = new java.util.ArrayList<Department>();
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Department Management | VISWA GRID</title>
<style>
*{box-sizing:border-box}body{margin:0;background:#f4f7f5;color:#18352d;font-family:Arial,sans-serif}
header{background:#103c32;color:white;padding:22px 7%;display:flex;justify-content:space-between;align-items:center;gap:15px}
header strong{font-size:23px}header a{color:white;text-decoration:none}
main{max-width:1100px;margin:35px auto;padding:0 20px}
h1{margin-bottom:8px} .muted{color:#718179;line-height:1.6}
.card{background:white;border:1px solid #e4ece7;border-radius:12px;padding:25px;margin:22px 0;box-shadow:0 3px 12px #00000008}
form.add{display:grid;grid-template-columns:1fr 2fr auto;gap:12px;align-items:end}
label{display:block;font-size:13px;font-weight:bold;margin-bottom:7px}
input,textarea,select{width:100%;padding:12px;border:1px solid #d8e3dc;border-radius:7px;font:inherit}
button{background:#176b4d;color:white;border:0;padding:12px 17px;border-radius:7px;font-weight:bold;cursor:pointer}
button:hover{background:#10543b}.table-wrap{overflow-x:auto}table{width:100%;border-collapse:collapse;min-width:700px}
th,td{text-align:left;padding:14px 12px;border-bottom:1px solid #edf1ee;font-size:13px}th{background:#f0f6f2;color:#496258}
.inline{display:grid;grid-template-columns:1.2fr 2fr 110px 90px;gap:9px;align-items:center}
.badge{padding:6px 9px;border-radius:20px;font-size:11px;font-weight:bold;background:#e0f5e8;color:#207747}
.inactive{background:#fff0d8;color:#8c6200}.back{display:inline-block;margin:10px 0;color:#176b4d;text-decoration:none;font-weight:bold}
.notice{padding:12px;background:#e3f6e9;border-radius:8px;color:#216d3e;margin:15px 0}
@media(max-width:700px){form.add{grid-template-columns:1fr}.card{padding:17px}}
</style>
</head>
<body>
<header><strong>VISWA <span style="color:#61d6a3">GRID</span> | Departments</strong><a href="<%=request.getContextPath()%>/admin-dashboard">Back to Dashboard</a></header>
<main>
<h1>Department Management</h1>
<p class="muted">Create departments and control which departments are available for complaint assignment.</p>
<a class="back" href="<%=request.getContextPath()%>/admin-dashboard">← Return to Admin Dashboard</a>
<% if ("1".equals(request.getParameter("saved"))) { %><div class="notice">Department details saved successfully.</div><% } %>
<% if (error != null) { %><div class="notice"><%=error%></div><% } %>
<section class="card">
<h2>Add New Department</h2>
<form class="add" method="post" action="<%=request.getContextPath()%>/admin-departments">
<div><label for="departmentName">Department Name</label><input id="departmentName" name="departmentName" maxlength="100" required placeholder="e.g. Roads & Transportation"></div>
<div><label for="description">Description</label><input id="description" name="description" maxlength="500" placeholder="Responsibilities of this department"></div>
<button type="submit">Add Department</button>
</form>
</section>
<section class="card">
<h2>Existing Departments</h2>
<p class="muted">You can update a department or deactivate it. Inactive departments cannot receive new complaint assignments.</p>
<div class="table-wrap"><table>
<thead><tr><th>Department</th><th>Description</th><th>Status</th><th>Action</th></tr></thead>
<tbody>
<% if (departments.isEmpty()) { %><tr><td colspan="4">No departments created yet.</td></tr><% } %>
<% for (Department dep : departments) { %>
<tr><td colspan="4">
<form class="inline" method="post" action="<%=request.getContextPath()%>/admin-departments">
<input type="text" name="departmentName" maxlength="100" required value="<%=escapeHtml(dep.getDepartmentName())%>">
<input type="text" name="description" maxlength="500" value="<%=escapeHtml(dep.getDescription())%>">
<select name="active"><option value="true" <%=dep.isActive()?"selected":""%>>Active</option><option value="false" <%=!dep.isActive()?"selected":""%>>Inactive</option></select>
<input type="hidden" name="departmentId" value="<%=dep.getDepartmentId()%>"><button type="submit">Save</button>
</form>
</td></tr>
<% } %>
</tbody></table></div>
</section>
</main>
</body></html>
