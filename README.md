# VISWA GRID
### Smart Civic Issue Reporting and Resolution System

VISWA GRID is a Java-based web application for reporting and mREADME.mdanaging civic issues such as potholes, damaged roads, garbage accumulation, non-functional streetlights and water leakage. It provides separate access for citizens, administrators and department officers so that complaints can be submitted, reviewed, assigned and tracked through a common platform.

The project is developed as an academic web application using Java, Jakarta Servlets, JSP, JDBC and MySQL.

---

## Table of Contents
- [Project Objectives](#project-objectives)
- [Main Features](#main-features)
- [Technology Stack](#technology-stack)
- [How the Application Works](#how-the-application-works)
- [Application Architecture](#application-architecture)
- [Code Structure](#code-structure)
- [Important Classes and Their Responsibilities](#important-classes-and-their-responsibilities)
- [Database Design](#database-design)
- [Security](#security)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Project Documentation](#project-documentation)
- [Current Workflow Limitation](#current-workflow-limitation)
- [Team](#team)

---

## Project Objectives
1. Provide citizens with a simple way to report civic problems.
2. Collect issue details, address information, map coordinates and optional photo evidence.
3. Allow administrators to review complaints and manage departments.
4. Assign complaints to the department responsible for the issue.
5. Give department officers access to complaints assigned to their department.
6. Allow citizens to track complaint progress using a complaint reference ID.

## Main Features

### Citizen module
- Citizen registration and login.
- Submit a complaint with category, title, description, address, city and PIN code.
- Select the issue location using latitude and longitude.
- Upload an optional issue photograph. The servlet accepts JPG, PNG and WEBP files, with a 5 MB per-file limit.
- View complaints associated with the logged-in citizen.
- Track a complaint using its reference ID.

### Administrator module
- Administrator login and logout.
- View and manage submitted complaints.
- Update complaint status.
- Add departments, edit department details, and activate or deactivate departments.
- Assign a complaint to an active department.
- Create and manage department officer accounts.

### Department officer module
- Separate department officer login and logout.
- View complaints assigned to the officer's department.
- Update the status of complaints belonging to that department.
- Officer accounts are created by an administrator; there is no public officer registration in the current implementation.

## Technology Stack

| Area | Technology |
|---|---|
| Programming language | Java 17 |
| Web application | Jakarta Servlets 6.0 |
| View layer | JSP 3.1, HTML, CSS and JavaScript |
| Database | MySQL 8 |
| Database connectivity | JDBC |
| JDBC driver | MySQL Connector/J 8.4.0 |
| Password hashing | jBCrypt 0.4 |
| Build and dependency management | Apache Maven |
| Application server | Apache Tomcat 10.1 |
| IDE used during development | Eclipse IDE |
| Version control | Git and GitHub |
| Packaging | WAR |

---

## How the Application Works

The application follows a request-and-response flow:

1. A user opens a page in the browser.
2. The browser submits a request to a URL handled by a Jakarta Servlet.
3. The servlet checks the submitted data and, where required, checks the user's session.
4. The servlet calls the relevant DAO (Data Access Object) class.
5. The DAO uses JDBC and a prepared SQL statement to communicate with MySQL.
6. The DAO returns the result to the servlet.
7. The servlet forwards the result to a JSP page or redirects the browser to another page.

### Example: Complaint submission
1. A citizen logs in and opens the complaint form.
2. The citizen enters the issue category, title, description and address, selects a map location and optionally attaches a photograph.
3. `ComplaintServlet` receives the multipart form submission at `/submit-complaint`.
4. The servlet checks the citizen session, validates required fields, validates the six-digit PIN code and checks the latitude and longitude values.
5. If a supported photograph is supplied, the servlet saves it in the web application's `uploads` directory using a generated filename.
6. The servlet creates a `Complaint` model object and calls `ComplaintDAO.addComplaint()`.
7. `ComplaintDAO` inserts the complaint into the MySQL `complaints` table using a `PreparedStatement`.
8. MySQL generates a complaint ID, which is returned by the DAO and used by the application for the complaint reference.

### Example: Department assignment
1. An administrator selects a complaint and an active department.
2. `AssignComplaintDepartmentServlet` checks that an administrator session exists.
3. It calls `ComplaintDAO.assignComplaintToDepartment()`.
4. The DAO updates the complaint's department and sets its status to `ASSIGNED`, provided the selected department is active.
5. The administrator is redirected to the dashboard.

### Example: Department officer status update
1. An officer logs in through the department officer login page.
2. The officer dashboard loads complaints for the officer's department.
3. The officer submits a status update.
4. `DepartmentOfficerStatusServlet` reads the department ID from the authenticated officer's session.
5. `DepartmentOfficerDAO.updateComplaintStatus()` updates the complaint only when both the complaint ID and department ID match.
6. The allowed officer status values are `IN_PROGRESS`, `RESOLVED` and `REJECTED`.

---

## Application Architecture

The project uses a layered Java web application structure:

```text
Browser
  |
  | HTTP request
  v
HTML / JSP pages
  |
  | Form submission / response
  v
Jakarta Servlets and Filters
  |
  | Calls application data methods
  v
DAO classes
  |
  | JDBC + PreparedStatement
  v
MySQL database
```

- **Presentation layer:** HTML, CSS, JavaScript and JSP pages display forms, dashboards and complaint information.
- **Controller layer:** Servlets receive HTTP requests, validate input, check sessions and decide which page to display.
- **Filter layer:** Authentication filters protect selected restricted pages.
- **Model layer:** Java classes represent application data such as citizens, complaints, administrators and departments.
- **Data access layer:** DAO classes contain SQL queries and database operations.
- **Database layer:** MySQL stores citizen, complaint, administrator, department and department officer records.

## Code Structure

```text
Viswa-Grid-project/
├── database/
│   ├── department_management_migration.sql
│   └── department_officer_login_migration.sql
├── Project-Report/
│   ├── README.md
│   └── VISWA_GRID_Full_11_Sections_Report.docx
├── Presentation/
│   ├── README.md
│   └── VISWA_GRID_Project_Presentation1.pptx
├── src/
│   └── main/
│       ├── java/com/viswagrid/
│       │   ├── controller/
│       │   ├── dao/
│       │   ├── model/
│       │   └── util/
│       └── webapp/
│           ├── WEB-INF/
│           ├── JSP pages
│           ├── index.html
│           └── report-issue.html
├── DEPARTMENT_FEATURE_SETUP.txt
├── DEPARTMENT_OFFICER_SETUP.txt
├── pom.xml
└── README.md
```

## Important Classes and Their Responsibilities

### Controller package: `com.viswagrid.controller`

| Class | Responsibility |
|---|---|
| `CitizenRegisterServlet` | Handles citizen registration requests. |
| `CitizenLoginServlet` | Authenticates citizens and creates their session. |
| `CitizenDashboardServlet` | Loads complaints associated with the logged-in citizen. |
| `ComplaintServlet` | Validates and processes new complaint submissions, including optional photo uploads. |
| `TrackComplaintServlet` | Finds a complaint using its reference ID and displays its details. |
| `AdminLoginServlet` | Handles administrator login. |
| `AdminDashboardServlet` | Loads complaint information for the administrator dashboard. |
| `AdminAuthFilter` | Checks for an administrator session before allowing access to selected admin routes. |
| `AdminDepartmentServlet` | Handles department management requests. |
| `AdminOfficerManagementServlet` | Handles department officer management requests. |
| `AssignComplaintDepartmentServlet` | Assigns a complaint to an active department. |
| `UpdateComplaintStatusServlet` | Handles administrator complaint status updates. |
| `DepartmentOfficerLoginServlet` | Handles department officer authentication. |
| `DepartmentDashboardServlet` | Loads complaints for the officer's department. |
| `DepartmentOfficerStatusServlet` | Processes status updates submitted by department officers. |

### DAO package: `com.viswagrid.dao`

DAO means Data Access Object. These classes separate SQL and database operations from servlet request-handling code.

| Class | Responsibility |
|---|---|
| `CitizenDAO` | Checks email availability, registers citizens and validates citizen login credentials. |
| `ComplaintDAO` | Inserts complaints, retrieves complaints, updates complaint status and assigns departments. |
| `AdminDAO` | Validates administrator credentials. |
| `DepartmentDAO` | Lists, adds, edits and activates/deactivates departments. |
| `DepartmentOfficerDAO` | Creates and authenticates officers, retrieves department complaints and updates permitted complaint statuses. |

### Model package: `com.viswagrid.model`

The model classes hold the data used by the application.

- `Citizen.java` represents a citizen account.
- `Complaint.java` represents a civic complaint, including its description, location, status and department information.
- `Admin.java` represents an administrator account.
- `Department.java` represents a department and its active/inactive state.

### Utility package: `com.viswagrid.util`

- `DBConnection.java` creates MySQL connections using JDBC. It reads the database username and password from the `VISWA_DB_USER` and `VISWA_DB_PASSWORD` environment variables.
- `PasswordUtil.java` hashes passwords using BCrypt and checks a supplied password against a stored hash.
- `CreateAdminHash.java` is a utility for generating an administrator password hash.
- `DBConnectionTest.java` is a database connection test utility.

---

## Database Design

The application connects to a MySQL database named `viswa_grid`.

The code uses tables including:

- `citizens` for citizen account details and password hashes.
- `admins` for administrator login details.
- `complaints` for complaint details, status, location and department assignment.
- `departments` for department names, descriptions and active state.
- `department_officers` for officer login details and their department relationship.

The department-related database changes are supplied as migration scripts in the `database/` directory:

1. `department_management_migration.sql`
2. `department_officer_login_migration.sql`

Run the department management migration first, followed by the department officer login migration. The setup notes in the repository also explain the order and prerequisites. Back up an existing database before running migration scripts.

### JDBC flow
1. `DBConnection.getConnection()` creates a connection using the MySQL JDBC driver.
2. A DAO prepares an SQL query using `PreparedStatement`.
3. Input values are passed through statement parameters.
4. The DAO executes the query or update.
5. A `ResultSet` is used when rows are returned.
6. The DAO maps database rows into model objects and returns them to the calling servlet.

---

## Security

- Passwords are stored as BCrypt hashes through `PasswordUtil`, rather than being stored as plain text.
- SQL values are passed through `PreparedStatement` parameters.
- Citizen, administrator and department officer routes check the relevant login session in their servlet logic.
- `AdminAuthFilter` protects the configured administrator routes.
- Department officer complaint updates include both the complaint ID and the officer's department ID in the update condition.

**Configuration note:** Do not commit database passwords, private credentials or real user data to GitHub. Configure `VISWA_DB_USER` and `VISWA_DB_PASSWORD` in the environment used by Tomcat.

---

## Setup and Installation

### Prerequisites
Install and configure:

- JDK 17
- Apache Maven
- MySQL 8
- Apache Tomcat 10.1
- Eclipse IDE (or another Java IDE that supports Maven web applications)

### 1. Clone the repository

```bash
git clone https://github.com/prajwalkondaveeti0039/Viswa-Grid-project.git
```

Open the cloned `Viswa-Grid-project` folder.

### 2. Create the database

Create a MySQL database named:

```sql
CREATE DATABASE viswa_grid;
```

The repository contains the department-related migration scripts. The base database tables must also exist before applying those migrations. Use the SQL schema supplied by the project team or the existing project database setup.

### 3. Configure database credentials

Set these environment variables for the Tomcat process:

```text
VISWA_DB_USER=your_mysql_username
VISWA_DB_PASSWORD=your_mysql_password
```

Replace the example values with your own local MySQL credentials. Do not put real credentials in source files or commit them to the repository.

The JDBC URL in `DBConnection.java` is:

```text
jdbc:mysql://localhost:3306/viswa_grid
```

### 4. Apply department migrations

Run the SQL scripts in this order:

1. `database/department_management_migration.sql`
2. `database/department_officer_login_migration.sql`

These scripts extend an existing project database. Review the setup notes and back up the database before applying them. Do not blindly rerun a migration if its columns or tables already exist.

### 5. Import the Maven project

In Eclipse:

1. Select **File → Import**.
2. Choose **Maven → Existing Maven Projects**.
3. Select the cloned project folder.
4. Finish the import.
5. Right-click the project and select **Maven → Update Project**.

### 6. Configure Tomcat

1. Add Apache Tomcat 10.1 in Eclipse's Servers view.
2. Configure the `VISWA_DB_USER` and `VISWA_DB_PASSWORD` environment variables for the Tomcat server process.
3. Add the project to the Tomcat server.

## Running the Application

1. Start MySQL.
2. Start the Tomcat 10.1 server from Eclipse.
3. Run the project on the server.
4. Open the application in a browser using the local Tomcat address and the deployed context path. With the Maven WAR final name, the context path is commonly `/viswa-grid` when deployed without a custom context name.

The welcome page is `index.html`.

---

## Project Documentation

The repository includes the project report and presentation:

- **Project report:** `Project-Report/VISWA_GRID_Full_11_Sections_Report.docx`
- **Presentation:** `Presentation/VISWA_GRID_Project_Presentation1.pptx`

The repository also includes setup notes for department management and department officer login:

- `DEPARTMENT_FEATURE_SETUP.txt`
- `DEPARTMENT_OFFICER_SETUP.txt`

## Current Workflow Limitation

In the current implementation, department officers can set an assigned complaint's status to `RESOLVED`. The code does not implement a separate citizen confirmation or independent physical verification step before that status is displayed as resolved. This is a limitation of the current version and a possible future enhancement.

## Team

VISWA GRID was developed as an academic project by the project team:

- Satya Harsha Tirumalasetti
- Viswa Naga Sri Harshith Choda
- Sahith Paruchuri
- Kondaveeti Prajwal Kumar

**Project Guide:** Dr. Srikanth Lukka

---

*VISWA GRID is an academic project prototype for civic issue reporting and complaint coordination. It is not represented as an officially integrated government or municipal complaint portal.*
