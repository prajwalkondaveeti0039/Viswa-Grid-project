<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("admin") != null) {
        response.sendRedirect(
            request.getContextPath() + "/admin-dashboard.jsp");
        return;
    }

    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Admin Login | VISWA GRID</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: Arial, sans-serif;
}

body {
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #e9f5ed, #d5e9dc);
    padding: 20px;
}

.login-card {
    width: 100%;
    max-width: 420px;
    background: white;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 12px 35px rgba(20, 70, 45, 0.12);
}

.brand {
    text-align: center;
    color: #16834a;
    font-size: 25px;
    font-weight: bold;
    margin-bottom: 10px;
}

h1 {
    text-align: center;
    color: #193e30;
    font-size: 25px;
    margin: 25px 0 10px;
}

.subtitle {
    text-align: center;
    color: #7b8b81;
    font-size: 14px;
    margin-bottom: 30px;
}

label {
    display: block;
    color: #344c3f;
    font-weight: bold;
    margin-bottom: 9px;
    font-size: 14px;
}

input {
    width: 100%;
    padding: 14px;
    border: 1px solid #d6e2da;
    border-radius: 8px;
    margin-bottom: 22px;
    font-size: 15px;
    outline: none;
}

input:focus {
    border-color: #16834a;
    box-shadow: 0 0 0 3px rgba(22, 131, 74, 0.1);
}

button {
    width: 100%;
    padding: 15px;
    border: none;
    border-radius: 8px;
    background: #16834a;
    color: white;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
}

button:hover {
    background: #10663a;
}

.error {
    background: #fff0f0;
    color: #b52b2b;
    padding: 12px;
    border-radius: 7px;
    margin-bottom: 20px;
    font-size: 14px;
}

.back {
    display: block;
    text-align: center;
    margin-top: 25px;
    color: #16834a;
    text-decoration: none;
    font-size: 14px;
}
</style>
</head>

<body>

<div class="login-card">

    <div class="brand">VISWA GRID</div>

    <h1>Administrator Login</h1>

    <p class="subtitle">
        Sign in to manage civic complaints and resolutions.
    </p>

    <% if (error != null) { %>
        <div class="error"><%= error %></div>
    <% } %>

    <form action="admin-login" method="post">

        <label for="username">Username</label>

        <input
            type="text"
            id="username"
            name="username"
            placeholder="Enter administrator username"
            autocomplete="username"
            required>

        <label for="password">Password</label>

        <input
            type="password"
            id="password"
            name="password"
            placeholder="Enter your password"
            autocomplete="current-password"
            required>

        <button type="submit">Sign In</button>

    </form>

    <a href="index.html" class="back">
        ← Back to Home
    </a>

</div>

</body>
</html>