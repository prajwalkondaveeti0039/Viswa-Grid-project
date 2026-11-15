<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Citizen Login | VISWA GRID</title>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f1f6f3;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 25px;
    color: #18392f;
}

.container {
    width: 100%;
    max-width: 450px;
    background: white;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 12px 40px rgba(16, 60, 50, 0.09);
}

.logo {
    text-align: center;
    font-size: 27px;
    font-weight: bold;
    letter-spacing: 1px;
    color: #103c32;
}

.logo span {
    color: #24966b;
}

.subtitle {
    text-align: center;
    color: #78877f;
    font-size: 12px;
    margin-top: 7px;
    margin-bottom: 32px;
}

h2 {
    font-size: 24px;
    margin-bottom: 9px;
}

.description {
    color: #77867e;
    font-size: 13px;
    line-height: 1.6;
    margin-bottom: 27px;
}

.form-group {
    margin-bottom: 20px;
}

label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    margin-bottom: 9px;
    color: #345347;
}

input {
    width: 100%;
    padding: 14px;
    border: 1px solid #dce7e0;
    border-radius: 8px;
    outline: none;
    font-size: 14px;
}

input:focus {
    border-color: #24966b;
    box-shadow: 0 0 0 3px rgba(36, 150, 107, 0.1);
}

button {
    width: 100%;
    padding: 14px;
    border: none;
    border-radius: 8px;
    background: #176b4d;
    color: white;
    font-size: 15px;
    font-weight: bold;
    cursor: pointer;
    margin-top: 8px;
}

button:hover {
    background: #10543b;
}

.error {
    background: #fff0f0;
    color: #a12e2e;
    border: 1px solid #ffd6d6;
    padding: 12px;
    border-radius: 7px;
    font-size: 13px;
    margin-bottom: 18px;
}

.links {
    text-align: center;
    font-size: 13px;
    margin-top: 24px;
    color: #738279;
    line-height: 2;
}

.links a {
    color: #176b4d;
    font-weight: bold;
    text-decoration: none;
}

.links a:hover {
    text-decoration: underline;
}

.back-link {
    display: block;
    text-align: center;
    margin-top: 19px;
    color: #718078;
    text-decoration: none;
    font-size: 13px;
}

@media(max-width: 500px) {
    .container {
        padding: 28px 22px;
    }
}
</style>
</head>

<body>

<div class="container">

    <div class="logo">VISWA <span>GRID</span></div>

    <p class="subtitle">
        SMART CIVIC ISSUE REPORTING SYSTEM
    </p>

    <h2>Citizen Login</h2>

    <p class="description">
        Login to your citizen account to report civic issues,
        track complaints and monitor their progress.
    </p>

    <% if (error != null) { %>
        <div class="error">
            <%= escapeHtml(error) %>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/citizen-login"
          method="post">

        <div class="form-group">

            <label for="email">Email Address</label>

            <input
                type="email"
                id="email"
                name="email"
                placeholder="Enter your registered email"
                maxlength="150"
                autocomplete="email"
                required>

        </div>

        <div class="form-group">

            <label for="password">Password</label>

            <input
                type="password"
                id="password"
                name="password"
                placeholder="Enter your password"
                autocomplete="current-password"
                required>

        </div>

        <button type="submit">
            Login to Account
        </button>

    </form>

    <div class="links">

        Don't have an account?

        <a href="<%= request.getContextPath() %>/citizen-register.jsp">
            Register here
        </a>

    </div>

    <a class="back-link"
       href="<%= request.getContextPath() %>/index.html">

        ← Back to Home

    </a>

</div>

</body>
</html>