<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.viswagrid.model.Complaint" %>
<%
    Complaint complaint =
        (Complaint) request.getAttribute("complaint");

    String error = (String) request.getAttribute("error");
    String referenceId =
        (String) request.getAttribute("referenceId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Track Complaint | VISWA GRID</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background: #f3f8f5;
            color: #263b31;
            min-height: 100vh;
        }

        nav {
            background: white;
            padding: 20px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 12px rgba(0,0,0,0.05);
        }

        .brand {
            color: #147b49;
            font-size: 23px;
            font-weight: bold;
        }

        nav a {
            color: #365044;
            text-decoration: none;
            font-weight: bold;
        }

        .container {
            width: 90%;
            max-width: 750px;
            margin: 55px auto;
        }

        .card {
            background: white;
            padding: 35px;
            border-radius: 14px;
            box-shadow: 0 5px 25px rgba(0,0,0,0.06);
            margin-bottom: 25px;
        }

        h1 {
            color: #155b40;
            margin-bottom: 12px;
        }

        .subtitle {
            color: #718078;
            line-height: 1.6;
            margin-bottom: 25px;
        }

        form {
            display: flex;
            gap: 12px;
        }

        input {
            flex: 1;
            min-width: 0;
            padding: 14px;
            border: 1px solid #d7e3dc;
            border-radius: 7px;
            font-size: 15px;
        }

        button {
            padding: 14px 22px;
            border: none;
            border-radius: 7px;
            background: #16834a;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #10663a;
        }

        .error {
            background: #fff0f0;
            color: #bd2929;
            padding: 13px;
            border-radius: 7px;
            margin-top: 18px;
        }

        .details h2 {
            color: #155b40;
            margin-bottom: 20px;
        }

        .detail {
            padding: 15px 0;
            border-bottom: 1px solid #edf1ee;
        }

        .detail-label {
            color: #829087;
            font-size: 13px;
            margin-bottom: 6px;
        }

        .detail-value {
            color: #263b31;
            font-weight: bold;
            overflow-wrap: anywhere;
        }

        .status {
            display: inline-block;
            background: #e3f5e9;
            color: #137b49;
            padding: 8px 15px;
            border-radius: 20px;
            font-weight: bold;
        }

        footer {
            text-align: center;
            padding: 25px;
            background: #173e32;
            color: white;
            font-size: 13px;
        }

        @media(max-width: 550px) {
            form {
                flex-direction: column;
            }

            .card {
                padding: 24px;
            }
        }
    </style>
</head>

<body>

<nav>
    <div class="brand">VISWA GRID</div>
    <a href="index.html">Home</a>
</nav>

<div class="container">

    <div class="card">

        <h1>Track Your Complaint</h1>

        <p class="subtitle">
            Enter your complaint reference ID to view
            the latest status and complaint details.
        </p>

        <form action="track-complaint" method="post">

            <input
                type="text"
                name="complaintId"
                placeholder="Example: VG-00005"
                value="<%= referenceId == null ? "" :
                    referenceId.replace("&", "&amp;")
                               .replace("\"", "&quot;")
                               .replace("<", "&lt;")
                               .replace(">", "&gt;") %>"
                required>

            <button type="submit">Track Complaint</button>

        </form>

        <% if (error != null) { %>
            <div class="error">
                <%= error %>
            </div>
        <% } %>

    </div>

    <% if (complaint != null) { %>

    <div class="card details">

        <h2>Complaint Details</h2>

        <div class="detail">
            <div class="detail-label">Reference ID</div>
            <div class="detail-value"><%= referenceId %></div>
        </div>

        <div class="detail">
            <div class="detail-label">Citizen Name</div>
            <div class="detail-value"><%= complaint.getCitizenName() %></div>
        </div>

        <div class="detail">
            <div class="detail-label">Category</div>
            <div class="detail-value"><%= complaint.getCategory() %></div>
        </div>

        <div class="detail">
            <div class="detail-label">Issue Title</div>
            <div class="detail-value"><%= complaint.getIssueTitle() %></div>
        </div>

        <div class="detail">
            <div class="detail-label">Description</div>
            <div class="detail-value"><%= complaint.getDescription() %></div>
        </div>

        <div class="detail">
            <div class="detail-label">Location</div>
            <div class="detail-value">
                <%= complaint.getAddress() %>,
                <%= complaint.getCity() %>,
                <%= complaint.getPincode() %>
            </div>
        </div>

        <div class="detail">
            <div class="detail-label">Current Status</div>
            <div class="detail-value">
                <span class="status">
                    <%= complaint.getStatus() %>
                </span>
            </div>
        </div>

    </div>

    <% } %>

</div>

<footer>
    © 2026 VISWA GRID | Smart Civic Issue Reporting and Resolution System
</footer>

</body>
</html>