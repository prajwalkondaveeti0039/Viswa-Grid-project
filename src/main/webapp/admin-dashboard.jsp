<%@ page import="java.util.List" %>
<%@ page import="com.viswagrid.model.Complaint" %>
<%@ page import="com.viswagrid.model.Admin" %>

<%!
    private String escapeHtml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    // Safely encode Java strings for JavaScript
    private String jsString(String value) {
        if (value == null) return "";

        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("'", "\\'")
                    .replace("\r", "\\r")
                    .replace("\n", "\\n")
                    .replace("\t", "\\t")
                    .replace("<", "\\u003C")
                    .replace(">", "\\u003E")
                    .replace("&", "\\u0026");
    }
%>

<%
    Admin admin = (Admin) session.getAttribute("admin");

    if (admin == null) {
        response.sendRedirect(request.getContextPath() + "/admin-login.jsp");
        return;
    }

    List<Complaint> complaints =
        (List<Complaint>) request.getAttribute("complaints");

    if (complaints == null) {
        complaints = new java.util.ArrayList<Complaint>();
    }

    int total = complaints.size();
    int submitted = 0;
    int assigned = 0;
    int inProgress = 0;
    int resolved = 0;
    int rejected = 0;
    int mappedCount = 0;

    for (Complaint c : complaints) {

        String status = c.getStatus();

        if ("SUBMITTED".equals(status)) submitted++;
        else if ("ASSIGNED".equals(status)) assigned++;
        else if ("IN_PROGRESS".equals(status)) inProgress++;
        else if ("RESOLVED".equals(status)) resolved++;
        else if ("REJECTED".equals(status)) rejected++;

        Double lat = c.getLatitude();
        Double lng = c.getLongitude();

        if (lat != null && lng != null
                && Double.isFinite(lat)
                && Double.isFinite(lng)
                && lat >= -90 && lat <= 90
                && lng >= -180 && lng <= 180) {
            mappedCount++;
        }
    }

    int unmappedCount = total - mappedCount;

    String adminName = admin.getFullName();

    String errorMessage = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Admin Dashboard | VISWA GRID</title>

<!-- LEAFLET MAP LIBRARY -->

<link rel="stylesheet"
      href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<style>

* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

html {
    scroll-behavior: smooth;
}

body {
    font-family: Arial, Helvetica, sans-serif;
    background: #f4f7f5;
    color: #18352d;
}

/* SIDEBAR */

.sidebar {
    width: 245px;
    height: 100vh;
    position: fixed;
    top: 0;
    left: 0;
    background: #103c32;
    color: white;
    padding: 28px 18px;
    display: flex;
    flex-direction: column;
    overflow-y: auto;
}

.logo {
    font-size: 25px;
    font-weight: bold;
    letter-spacing: 1px;
    margin: 5px 10px 8px;
}

.logo span {
    color: #61d6a3;
}

.sidebar p {
    font-size: 12px;
    color: #b8d1c8;
    margin: 0 10px 35px;
}

.nav-link {
    display: block;
    text-decoration: none;
    color: #dce9e4;
    padding: 14px 13px;
    border-radius: 8px;
    margin-bottom: 9px;
    font-size: 14px;
    transition: 0.2s;
}

.nav-link:hover,
.nav-link.active {
    background: #216b55;
    color: white;
}

.logout-form {
    margin-top: auto;
}

.logout-button {
    width: 100%;
    border: none;
    background: #a83e3e;
    color: white;
    padding: 13px;
    border-radius: 8px;
    cursor: pointer;
    font-size: 14px;
}

.logout-button:hover {
    background: #8e3030;
}

/* MAIN CONTENT */

.main {
    margin-left: 245px;
    padding: 30px 35px;
}

.topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    margin-bottom: 30px;
}

.topbar h1 {
    font-size: 27px;
}

.welcome {
    color: #63766e;
    font-size: 14px;
    margin-top: 7px;
}

.admin-label {
    background: white;
    padding: 12px 18px;
    border-radius: 9px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.04);
    font-size: 14px;
}

/* STATISTICS */

.stats {
    display: grid;
    grid-template-columns: repeat(5, minmax(130px, 1fr));
    gap: 16px;
    margin-bottom: 35px;
}

.stat-card {
    background: white;
    padding: 21px 17px;
    border-radius: 12px;
    border: 1px solid #e8efeb;
    box-shadow: 0 3px 12px rgba(0,0,0,0.03);
}

.stat-card p {
    color: #718179;
    font-size: 13px;
    margin-bottom: 13px;
}

.stat-card h2 {
    font-size: 29px;
    color: #175c46;
}

/* SECTIONS */

.section {
    background: white;
    border-radius: 13px;
    padding: 25px;
    border: 1px solid #e8efeb;
    box-shadow: 0 3px 12px rgba(0,0,0,0.03);
    scroll-margin-top: 20px;
    margin-bottom: 30px;
}

.section-heading {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 15px;
    margin-bottom: 22px;
}

.section-heading h2 {
    font-size: 21px;
}

.section-heading p {
    font-size: 13px;
    color: #7b8982;
    margin-top: 6px;
}

/* MAP */

#adminComplaintMap {
    width: 100%;
    height: 480px;
    border-radius: 10px;
    border: 1px solid #dce5df;
    z-index: 1;
}

.map-summary {
    display: flex;
    gap: 15px;
    flex-wrap: wrap;
    margin-bottom: 18px;
}

.map-stat {
    background: #f1f7f3;
    border: 1px solid #e0ebe3;
    padding: 12px 16px;
    border-radius: 8px;
    font-size: 13px;
}

.map-stat strong {
    color: #176b4d;
    font-size: 18px;
    display: block;
    margin-bottom: 4px;
}

.map-note {
    color: #77867e;
    font-size: 12px;
    margin-top: 13px;
    line-height: 1.6;
}

.map-empty {
    background: #f5f9f6;
    border: 1px solid #e0ebe3;
    padding: 15px;
    border-radius: 8px;
    color: #64776b;
    margin-top: 15px;
    font-size: 13px;
}

.leaflet-popup-content {
    line-height: 1.7;
    font-size: 13px;
    min-width: 190px;
}

.popup-title {
    font-weight: bold;
    color: #087b40;
    font-size: 14px;
    margin-bottom: 7px;
}

.popup-status {
    font-weight: bold;
    color: #176b4d;
}

/* FILTERS */

.filters {
    display: flex;
    gap: 12px;
    margin-bottom: 20px;
}

.filters input,
.filters select {
    padding: 12px;
    border: 1px solid #dce5df;
    border-radius: 7px;
    font-size: 13px;
    background: white;
    outline: none;
}

.filters input {
    flex: 1;
    min-width: 150px;
}

.filters input:focus,
.filters select:focus {
    border-color: #27855f;
}

/* TABLE */

.table-wrapper {
    width: 100%;
    overflow-x: auto;
}

table {
    width: 100%;
    border-collapse: collapse;
    min-width: 900px;
}

thead {
    background: #f0f6f2;
}

th {
    padding: 14px 12px;
    text-align: left;
    color: #496258;
    font-size: 12px;
    text-transform: uppercase;
    letter-spacing: 0.4px;
}

td {
    padding: 15px 12px;
    border-bottom: 1px solid #edf1ee;
    font-size: 13px;
    vertical-align: middle;
}

tbody tr:hover {
    background: #fafcfb;
}

/* STATUS */

.status {
    display: inline-block;
    padding: 6px 10px;
    border-radius: 20px;
    font-size: 11px;
    font-weight: bold;
    white-space: nowrap;
}

.status-submitted {
    background: #fff3d8;
    color: #9a6800;
}

.status-assigned {
    background: #e7efff;
    color: #315b9c;
}

.status-in_progress {
    background: #e8e1ff;
    color: #6343a3;
}

.status-resolved {
    background: #dff5e8;
    color: #207747;
}

.status-rejected {
    background: #ffe5e5;
    color: #a43d3d;
}

/* DETAILS */

.details {
    margin-top: 10px;
    min-width: 230px;
}

.details summary {
    color: #18744e;
    cursor: pointer;
    font-weight: bold;
    font-size: 12px;
}

.details-content {
    background: #f5f9f6;
    padding: 13px;
    margin-top: 10px;
    border-radius: 8px;
    line-height: 1.7;
    overflow-wrap: anywhere;
}

.details-content img {
    display: block;
    max-width: 180px;
    max-height: 150px;
    object-fit: contain;
    margin-top: 10px;
    border-radius: 6px;
}

/* UPDATE STATUS */

.update-form {
    display: flex;
    flex-direction: column;
    gap: 8px;
    min-width: 145px;
}

.update-form select {
    padding: 9px;
    border: 1px solid #dce5df;
    border-radius: 6px;
    background: white;
    font-size: 12px;
}

.update-button {
    border: none;
    background: #176b4d;
    color: white;
    padding: 9px;
    border-radius: 6px;
    cursor: pointer;
    font-size: 12px;
}

.update-button:hover {
    background: #10543b;
}

.empty {
    text-align: center;
    padding: 35px;
    color: #7b8982;
}

/* ALERTS */

.alert {
    padding: 13px 16px;
    margin-bottom: 20px;
    border-radius: 8px;
    font-size: 14px;
}

.alert-error {
    background: #ffe9e9;
    color: #a12e2e;
}

.alert-success {
    background: #e3f6e9;
    color: #216d3e;
}

/* RESPONSIVE */

@media(max-width: 1100px) {
    .stats {
        grid-template-columns: repeat(3, 1fr);
    }
}

@media(max-width: 750px) {

    .sidebar {
        width: 100%;
        height: auto;
        position: relative;
        padding: 15px;
    }

    .sidebar p {
        margin-bottom: 12px;
    }

    .logout-form {
        margin-top: 10px;
    }

    .main {
        margin-left: 0;
        padding: 20px 14px;
    }

    .stats {
        grid-template-columns: repeat(2, 1fr);
    }

    .topbar {
        align-items: flex-start;
        flex-direction: column;
    }

    .filters {
        flex-direction: column;
    }

    .section {
        padding: 16px;
    }

    #adminComplaintMap {
        height: 350px;
    }
}

</style>
</head>

<body>

<!-- SIDEBAR -->

<aside class="sidebar">

    <div class="logo">VISWA <span>GRID</span></div>

    <p>SMART CIVIC MANAGEMENT</p>

    <a class="nav-link active"
       href="<%= request.getContextPath() %>/admin-dashboard">
       Dashboard
    </a>

    <a class="nav-link" href="#complaintMapSection">
       Complaint Map
    </a>

    <a class="nav-link" href="#complaints">
       Manage Complaints
    </a>

    <a class="nav-link"
       href="<%= request.getContextPath() %>/index.html">
       View Website
    </a>

    <form class="logout-form"
          action="<%= request.getContextPath() %>/admin-logout"
          method="post">

        <button type="submit" class="logout-button">
            Logout
        </button>

    </form>

</aside>

<!-- MAIN CONTENT -->

<main class="main">

    <!-- HEADER -->

    <header class="topbar">

        <div>
            <h1>Admin Dashboard</h1>

            <p class="welcome">
                Monitor and manage civic complaints across the system.
            </p>
        </div>

        <div class="admin-label">
            Administrator:
            <strong><%= escapeHtml(adminName) %></strong>
        </div>

    </header>

    <% if (errorMessage != null) { %>

        <div class="alert alert-error">
            <%= escapeHtml(errorMessage) %>
        </div>

    <% } %>

    <% if ("1".equals(request.getParameter("updated"))) { %>

        <div class="alert alert-success">
            Complaint status updated successfully.
        </div>

    <% } %>

    <!-- STATISTICS -->

    <section class="stats">

        <div class="stat-card">
            <p>Total Complaints</p>
            <h2><%= total %></h2>
        </div>

        <div class="stat-card">
            <p>Submitted</p>
            <h2><%= submitted %></h2>
        </div>

        <div class="stat-card">
            <p>Assigned</p>
            <h2><%= assigned %></h2>
        </div>

        <div class="stat-card">
            <p>In Progress</p>
            <h2><%= inProgress %></h2>
        </div>

        <div class="stat-card">
            <p>Resolved</p>
            <h2><%= resolved %></h2>
        </div>

    </section>

    <!-- COMPLAINT MAP SECTION -->

    <section class="section" id="complaintMapSection">

        <div class="section-heading">

            <div>
                <h2>Complaint Location Map</h2>

                <p>
                    View the geographical locations of reported civic issues.
                </p>
            </div>

        </div>

        <div class="map-summary">

            <div class="map-stat">
                <strong><%= mappedCount %></strong>
                Complaints with locations
            </div>

            <div class="map-stat">
                <strong><%= unmappedCount %></strong>
                Complaints without locations
            </div>

        </div>

        <div id="adminComplaintMap"></div>

        <p class="map-note">
            Select a map marker to view the complaint ID, citizen name,
            issue category, address and current status.
            Only complaints with valid saved coordinates are displayed.
        </p>

        <% if (mappedCount == 0) { %>

            <div class="map-empty">
                No complaint locations are available yet.
                Submit a new complaint after selecting a location on the
                citizen map to see its marker here.
            </div>

        <% } else if (unmappedCount > 0) { %>

            <div class="map-empty">
                <%= unmappedCount %> older complaint(s) do not have
                saved coordinates. They will not appear on this map.
                New complaints submitted with a selected location
                will appear automatically.
            </div>

        <% } %>

    </section>

    <!-- MANAGE COMPLAINTS SECTION -->

    <section class="section" id="complaints">

        <div class="section-heading">

            <div>
                <h2>Manage Complaints</h2>

                <p>
                    Review complaint details and update their current status.
                </p>
            </div>

        </div>

        <!-- SEARCH AND FILTER -->

        <div class="filters">

            <input type="text"
                   id="searchInput"
                   placeholder="Search by ID, citizen, category, city...">

            <select id="statusFilter">

                <option value="">All Statuses</option>
                <option value="SUBMITTED">Submitted</option>
                <option value="ASSIGNED">Assigned</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="RESOLVED">Resolved</option>
                <option value="REJECTED">Rejected</option>

            </select>

        </div>

        <!-- COMPLAINT TABLE -->

        <div class="table-wrapper">

            <table id="complaintTable">

                <thead>

                    <tr>
                        <th>Reference ID</th>
                        <th>Citizen</th>
                        <th>Category</th>
                        <th>Issue</th>
                        <th>City</th>
                        <th>Status</th>
                        <th>Details</th>
                        <th>Update Status</th>
                    </tr>

                </thead>

                <tbody>

                <% if (complaints.isEmpty()) { %>

                    <tr>
                        <td colspan="8" class="empty">
                            No complaints have been submitted yet.
                        </td>
                    </tr>

                <% } else { %>

                    <% for (Complaint c : complaints) {

                        String currentStatus = c.getStatus() == null
                                ? "SUBMITTED" : c.getStatus();

                        String statusClass = currentStatus.toLowerCase();

                    %>

                    <tr class="complaint-row"
                        data-status="<%= escapeHtml(currentStatus) %>">

                        <td>
                            #<%= c.getComplaintId() %>
                        </td>

                        <td>
                            <%= escapeHtml(c.getCitizenName()) %>
                        </td>

                        <td>
                            <%= escapeHtml(c.getCategory()) %>
                        </td>

                        <td>
                            <%= escapeHtml(c.getIssueTitle()) %>
                        </td>

                        <td>
                            <%= escapeHtml(c.getCity()) %>
                        </td>

                        <td>

                            <span class="status status-<%= escapeHtml(statusClass) %>">

                                <%= escapeHtml(currentStatus.replace("_", " ")) %>

                            </span>

                        </td>

                        <!-- VIEW DETAILS -->

                        <td>

                            <details class="details">

                                <summary>View Details</summary>

                                <div class="details-content">

                                    <strong>Description:</strong><br>

                                    <%= escapeHtml(c.getDescription()) %>

                                    <br><br>

                                    <strong>Address:</strong><br>

                                    <%= escapeHtml(c.getAddress()) %>

                                    <br><br>

                                    <strong>City:</strong>
                                    <%= escapeHtml(c.getCity()) %>

                                    <br>

                                    <strong>PIN Code:</strong>
                                    <%= escapeHtml(c.getPincode()) %>

                                    <% if (c.getLatitude() != null
                                            && c.getLongitude() != null) { %>

                                        <br><br>

                                        <strong>Latitude:</strong>
                                        <%= c.getLatitude() %>

                                        <br>

                                        <strong>Longitude:</strong>
                                        <%= c.getLongitude() %>

                                    <% } else { %>

                                        <br><br>
                                        <strong>Map location:</strong>
                                        Not available

                                    <% } %>

                                    <% if (c.getPhotoPath() != null
                                            && !c.getPhotoPath().trim().isEmpty()) { %>

                                        <br><br>

                                        <strong>Uploaded Photo:</strong>

                                        <a target="_blank"
                                           rel="noopener"
                                           href="<%= request.getContextPath() %>/<%= escapeHtml(c.getPhotoPath()) %>">

                                            <img
                                                src="<%= request.getContextPath() %>/<%= escapeHtml(c.getPhotoPath()) %>"
                                                alt="Complaint photo">

                                        </a>

                                    <% } %>

                                </div>

                            </details>

                        </td>

                        <!-- UPDATE STATUS -->

                        <td>

                            <form class="update-form"
                                  action="<%= request.getContextPath() %>/update-complaint-status"
                                  method="post">

                                <input type="hidden"
                                       name="complaintId"
                                       value="<%= c.getComplaintId() %>">

                                <select name="status" required>

                                    <option value="SUBMITTED"
                                        <%= "SUBMITTED".equals(currentStatus) ? "selected" : "" %>>
                                        Submitted
                                    </option>

                                    <option value="ASSIGNED"
                                        <%= "ASSIGNED".equals(currentStatus) ? "selected" : "" %>>
                                        Assigned
                                    </option>

                                    <option value="IN_PROGRESS"
                                        <%= "IN_PROGRESS".equals(currentStatus) ? "selected" : "" %>>
                                        In Progress
                                    </option>

                                    <option value="RESOLVED"
                                        <%= "RESOLVED".equals(currentStatus) ? "selected" : "" %>>
                                        Resolved
                                    </option>

                                    <option value="REJECTED"
                                        <%= "REJECTED".equals(currentStatus) ? "selected" : "" %>>
                                        Rejected
                                    </option>

                                </select>

                                <button type="submit" class="update-button">
                                    Update Status
                                </button>

                            </form>

                        </td>

                    </tr>

                    <% } %>

                <% } %>

                </tbody>

            </table>

        </div>

    </section>

</main>

<!-- JAVASCRIPT -->

<script>

// ========================================
// COMPLAINT SEARCH AND FILTER
// ========================================

const searchInput = document.getElementById("searchInput");
const statusFilter = document.getElementById("statusFilter");

function filterComplaints() {

    const searchText = searchInput.value.toLowerCase().trim();
    const selectedStatus = statusFilter.value;

    const rows = document.querySelectorAll(".complaint-row");

    rows.forEach(function(row) {

        const rowText = row.innerText.toLowerCase();
        const rowStatus = row.getAttribute("data-status");

        const matchesSearch = rowText.includes(searchText);

        const matchesStatus =
            selectedStatus === "" || rowStatus === selectedStatus;

        row.style.display =
            matchesSearch && matchesStatus ? "" : "none";

    });
}

searchInput.addEventListener("input", filterComplaints);
statusFilter.addEventListener("change", filterComplaints);


// ========================================
// ADMIN COMPLAINT LOCATION MAP
// ========================================

// Complaint information generated from the database

const complaintLocations = [

<%
    boolean firstLocation = true;

    for (Complaint c : complaints) {

        Double lat = c.getLatitude();
        Double lng = c.getLongitude();

        if (lat == null || lng == null
                || !Double.isFinite(lat)
                || !Double.isFinite(lng)
                || lat < -90 || lat > 90
                || lng < -180 || lng > 180) {
            continue;
        }

        if (!firstLocation) {
%>
,
<%
        }

        firstLocation = false;
%>
{
    id: <%= c.getComplaintId() %>,
    citizen: "<%= jsString(c.getCitizenName()) %>",
    category: "<%= jsString(c.getCategory()) %>",
    issue: "<%= jsString(c.getIssueTitle()) %>",
    address: "<%= jsString(c.getAddress()) %>",
    city: "<%= jsString(c.getCity()) %>",
    pincode: "<%= jsString(c.getPincode()) %>",
    status: "<%= jsString(c.getStatus()) %>",
    latitude: <%= lat %>,
    longitude: <%= lng %>
}
<%
    }
%>

];


// ========================================
// INITIALIZE LEAFLET MAP
// ========================================

const adminMap = L.map("adminComplaintMap").setView(
    [20.5937, 78.9629],
    5
);

L.tileLayer(
    "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
    {
        attribution:
            '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        maxZoom: 19
    }
).addTo(adminMap);


// ========================================
// ADD COMPLAINT MARKERS
// ========================================

const markerBounds = [];

complaintLocations.forEach(function(complaint) {

    const marker = L.marker([
        complaint.latitude,
        complaint.longitude
    ]).addTo(adminMap);

    // Build popup using textContent to avoid injecting
    // complaint text as executable HTML.

    const popup = document.createElement("div");

    const title = document.createElement("div");
    title.className = "popup-title";
    title.textContent = "Complaint #" + complaint.id;

    popup.appendChild(title);

    function addPopupLine(label, value, className) {

        const line = document.createElement("div");

        const strong = document.createElement("strong");
        strong.textContent = label + ": ";

        line.appendChild(strong);

        const span = document.createElement("span");
        span.textContent = value || "Not provided";

        if (className) {
            span.className = className;
        }

        line.appendChild(span);

        popup.appendChild(line);
    }

    addPopupLine("Citizen", complaint.citizen);
    addPopupLine("Category", complaint.category);
    addPopupLine("Issue", complaint.issue);
    addPopupLine("Address", complaint.address);
    addPopupLine("City", complaint.city);
    addPopupLine("PIN Code", complaint.pincode);
    addPopupLine("Status", complaint.status, "popup-status");

    marker.bindPopup(popup);

    markerBounds.push([
        complaint.latitude,
        complaint.longitude
    ]);

});


// ========================================
// AUTOMATICALLY ZOOM TO ALL MARKERS
// ========================================

if (markerBounds.length > 0) {

    adminMap.fitBounds(markerBounds, {
        padding: [40, 40],
        maxZoom: 15
    });

} else {

    adminMap.setView([20.5937, 78.9629], 5);

}


// ========================================
// FIX MAP SIZE WHEN OPENING MAP SECTION
// ========================================

document.querySelectorAll(
    'a[href="#complaintMapSection"]'
).forEach(function(link) {

    link.addEventListener("click", function() {

        setTimeout(function() {
            adminMap.invalidateSize();
        }, 250);

    });

});

</script>

</body>
</html>