<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String complaintId = (String) request.getAttribute("complaintId");

    if (complaintId == null) {
        response.sendRedirect("index.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Complaint Submitted | VISWA GRID</title>

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            min-height: 100vh;
            background: #f3f8f5;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }

        .success-card {
            background: white;
            width: 100%;
            max-width: 520px;
            padding: 45px 30px;
            border-radius: 16px;
            text-align: center;
            box-shadow: 0 8px 30px rgba(0,0,0,0.08);
        }

        .success-icon {
            width: 75px;
            height: 75px;
            background: #e1f5e8;
            color: #16834a;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 38px;
            margin: 0 auto 25px;
        }

        h1 {
            color: #155b40;
            margin-bottom: 15px;
            font-size: 28px;
        }

        .message {
            color: #64736b;
            line-height: 1.7;
            margin-bottom: 25px;
        }

        .complaint-box {
            background: #f0f8f3;
            padding: 20px;
            border-radius: 10px;
            margin-bottom: 25px;
        }

        .complaint-box p {
            color: #607268;
            margin-bottom: 8px;
        }

        .complaint-id {
            color: #137b49;
            font-size: 25px;
            font-weight: bold;
        }

        .home-btn {
            display: inline-block;
            background: #16834a;
            color: white;
            text-decoration: none;
            padding: 13px 28px;
            border-radius: 7px;
            font-weight: bold;
        }

        .home-btn:hover {
            background: #10663a;
        }
    </style>
</head>

<body>

    <div class="success-card">

        <div class="success-icon">✓</div>

        <h1>Complaint Submitted!</h1>

        <p class="message">
            Your civic issue has been successfully registered
            with VISWA GRID. Our team will review your complaint
            and take the necessary action.
        </p>

        <div class="complaint-box">
            <p>Your Complaint Reference ID</p>
            <div class="complaint-id">
                <%= complaintId %>
            </div>
        </div>

        <p class="message">
            Please save this reference ID for future tracking.
        </p>

        <a href="index.html" class="home-btn">
            Back to Home
        </a>

    </div>

</body>
</html>