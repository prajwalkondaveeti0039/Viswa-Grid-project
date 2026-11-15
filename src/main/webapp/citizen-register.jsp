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

<title>Citizen Registration | VISWA GRID</title>

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
    max-width: 480px;
    background: white;
    padding: 38px;
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
    font-size: 13px;
    margin-top: 7px;
    margin-bottom: 28px;
}

h2 {
    font-size: 23px;
    margin-bottom: 8px;
}

.description {
    color: #77867e;
    font-size: 13px;
    margin-bottom: 25px;
    line-height: 1.5;
}

.form-group {
    margin-bottom: 17px;
}

label {
    display: block;
    font-size: 13px;
    font-weight: bold;
    margin-bottom: 8px;
    color: #345347;
}

input {
    width: 100%;
    padding: 13px 14px;
    border: 1px solid #dce7e0;
    border-radius: 8px;
    outline: none;
    font-size: 14px;
    transition: 0.2s;
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

.login-link {
    text-align: center;
    font-size: 13px;
    margin-top: 23px;
    color: #738279;
}

.login-link a {
    color: #176b4d;
    font-weight: bold;
    text-decoration: none;
}

.back-link {
    display: block;
    text-align: center;
    margin-top: 17px;
    color: #718078;
    text-decoration: none;
    font-size: 13px;
}

.back-link:hover,
.login-link a:hover {
    text-decoration: underline;
}

.note {
    background: #f1f8f3;
    padding: 12px;
    border-radius: 7px;
    font-size: 12px;
    color: #567064;
    line-height: 1.5;
    margin-top: 18px;
}

@media(max-width: 500px) {
    .container {
        padding: 27px 21px;
    }
}
</style>
</head>

<body>

<div class="container">

    <div class="logo">VISWA <span>GRID</span></div>

    <p class="subtitle">SMART CIVIC ISSUE REPORTING SYSTEM</p>

    <h2>Create Citizen Account</h2>

    <p class="description">
        Register with VISWA GRID to report civic issues,
        track complaints and receive updates.
    </p>

    <% if (error != null) { %>
        <div class="error">
            <%= escapeHtml(error) %>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/citizen-register"
          method="post">

        <div class="form-group">
            <label for="fullName">Full Name</label>
            <input
                type="text"
                id="fullName"
                name="fullName"
                placeholder="Enter your full name"
                maxlength="100"
                autocomplete="name"
                required>
        </div>

        <div class="form-group">
            <label for="email">Email Address</label>
            <input
                type="email"
                id="email"
                name="email"
                placeholder="Enter your email address"
                maxlength="150"
                autocomplete="email"
                required>
        </div>

        <div class="form-group">
            <label for="mobile">Mobile Number</label>
            <input
                type="tel"
                id="mobile"
                name="mobile"
                placeholder="Enter your mobile number"
                maxlength="15"
                autocomplete="tel"
                required>
        </div>

        <div class="form-group">
            <label for="password">Create Password</label>
            <input
                type="password"
                id="password"
                name="password"
                placeholder="Minimum 8 characters"
                minlength="8"
                maxlength="72"
                autocomplete="new-password"
                required>
        </div>

        <div class="form-group">
            <label for="confirmPassword">Confirm Password</label>
            <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                placeholder="Re-enter your password"
                minlength="8"
                maxlength="72"
                autocomplete="new-password"
                required>
        </div>

        <button type="submit">
            Create Account
        </button>

    </form>

    <div class="note">
        Your password is securely stored using BCrypt hashing.
        Never share your login credentials with anyone.
    </div>

    <p class="login-link">
        Already have an account?
        <a href="<%= request.getContextPath() %>/citizen-login.jsp">
            Login here
        </a>
    </p>

    <a class="back-link"
       href="<%= request.getContextPath() %>/index.html">
        ← Back to Home
    </a>

</div>

</body>
</html>