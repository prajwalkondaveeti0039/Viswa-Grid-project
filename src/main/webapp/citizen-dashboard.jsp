<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.viswagrid.model.Citizen" %>
<%@ page import="com.viswagrid.model.Complaint" %>

<%
    Citizen citizen = (Citizen) session.getAttribute("citizen");

    if (citizen == null) {
        response.sendRedirect(request.getContextPath() + "/citizen-login.jsp");
        return;
    }

    List<Complaint> complaints =
        (List<Complaint>) request.getAttribute("complaints");

    if (complaints == null) {
        complaints = java.util.Collections.emptyList();
    }

    String citizenName = citizen.getFullName();
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Citizen Dashboard | VISWA GRID</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Segoe UI', Arial, sans-serif;
}

body {
    background: #f4f8f5;
    color: #17251e;
}

header {
    background: #ffffff;
    padding: 20px 6%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0,0,0,.07);
}

.logo {
    font-size: 24px;
    font-weight: 800;
    color: #17251e;
}

.logo span {
    color: #159653;
}

header a {
    color: #159653;
    text-decoration: none;
    font-weight: 700;
}

.container {
    width: 90%;
    max-width: 1200px;
    margin: 40px auto;
}

.welcome {
    background: linear-gradient(120deg, #087b40, #20ae68);
    color: white;
    padding: 35px;
    border-radius: 15px;
    margin-bottom: 30px;
}

.welcome h1 {
    margin-bottom: 10px;
    font-size: 30px;
}

.welcome p {
    line-height: 1.7;
    color: #e6f7ed;
}

.actions {
    display: flex;
    gap: 15px;
    flex-wrap: wrap;
    margin: 25px 0 35px;
}

.btn {
    display: inline-block;
    padding: 13px 22px;
    background: #159653;
    color: white;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 700;
}

.btn.secondary {
    background: white;
    color: #159653;
    border: 1px solid #159653;
}

.section-title {
    margin-bottom: 20px;
    font-size: 24px;
}

.table-container {
    background: white;
    border-radius: 12px;
    overflow-x: auto;
    box-shadow: 0 4px 20px rgba(0,0,0,.05);
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 750px;
}

th, td {
    padding: 17px;
    text-align: left;
    border-bottom: 1px solid #e8eee9;
    font-size: 14px;
}

th {
    background: #edf5ef;
    color: #087b40;
}

.status {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    background: #edf5ef;
    color: #087b40;
    font-size: 12px;
    font-weight: 700;
}

.empty {
    padding: 35px;
    text-align: center;
    color: #77827b;
}

footer {
    text-align: center;
    padding: 25px;
    color: #77827b;
    font-size: 13px;
}
</style>
</head>

<body>

<header>
    <div class="logo">VISWA <span>GRID</span></div>
    <a href="<%= request.getContextPath() %>/index.html">Back to Home</a>
</header>

<main class="container">

    <section class="welcome">
        <h1>Welcome, <%= citizenName %>!</h1>
        <p>
            Your citizen dashboard gives you access to your
            submitted complaints and their current progress.
        </p>
    </section>

    <div class="actions">
        <a class="btn"
           href="<%= request.getContextPath() %>/report-issue.html">
            + Report a New Issue
        </a>

        <a class="btn secondary"
           href="<%= request.getContextPath() %>/track-complaint.jsp">
            Track a Complaint
        </a>
    </div>

    <h2 class="section-title">My Complaints</h2>

    <div class="table-container">

        <table>
            <thead>
                <tr>
                    <th>Complaint ID</th>
                    <th>Category</th>
                    <th>Issue</th>
                    <th>City</th>
                    <th>Status</th>
                </tr>
            </thead>

            <tbody>

            <% if (complaints.isEmpty()) { %>

                <tr>
                    <td colspan="5" class="empty">
                        You haven't submitted any complaints yet.
                    </td>
                </tr>

            <% } else {
                for (Complaint complaint : complaints) { %>

                <tr>
                    <td>VG-<%= String.format("%05d",
                        complaint.getComplaintId()) %></td>

                    <td><%= complaint.getCategory() %></td>

                    <td><%= complaint.getIssueTitle() %></td>

                    <td><%= complaint.getCity() %></td>

                    <td>
                        <span class="status">
                            <%= complaint.getStatus() %>
                        </span>
                    </td>
                </tr>

            <%  }
               } %>

            </tbody>
        </table>

    </div>

</main>

<footer>
    © 2026 VISWA GRID | Smart Civic Issue Reporting & Resolution System
</footer>

</body>
</html>