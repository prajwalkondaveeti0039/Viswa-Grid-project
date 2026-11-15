package com.viswagrid.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import com.viswagrid.dao.ComplaintDAO;
import com.viswagrid.model.Citizen;
import com.viswagrid.model.Complaint;

@WebServlet("/submit-complaint")
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,
    maxRequestSize = 6 * 1024 * 1024
)
public class ComplaintServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 1. Verify citizen login
        HttpSession session = request.getSession(false);

        if (session == null
                || session.getAttribute("citizen") == null) {

            response.sendRedirect(
                request.getContextPath() + "/citizen-login.jsp"
            );
            return;
        }

        // 2. Get authenticated citizen
        Citizen citizen =
                (Citizen) session.getAttribute("citizen");

        // Validate citizen ID before processing
     // Validate citizen ID
        if (citizen.getCitizenId() <= 0) {

            session.invalidate();

            response.sendRedirect(
                request.getContextPath() + "/citizen-login.jsp"
            );
            return;
        }

        int citizenId = citizen.getCitizenId();
        String citizenName = citizen.getFullName();


        // 3. Receive complaint details
        String category = request.getParameter("category");
        String issueTitle = request.getParameter("issueTitle");
        String description = request.getParameter("description");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String pincode = request.getParameter("pincode");

        // 4. Receive map coordinates
        String latitudeValue = request.getParameter("latitude");
        String longitudeValue = request.getParameter("longitude");

        // 5. Validate required fields
        if (isBlank(citizenName)
                || isBlank(category)
                || isBlank(issueTitle)
                || isBlank(description)
                || isBlank(address)
                || isBlank(city)
                || isBlank(pincode)
                || !pincode.trim().matches("\\d{6}")) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Please enter all required details and a valid 6-digit PIN code."
            );
            return;
        }

        // 6. Validate map coordinates
        if (isBlank(latitudeValue)
                || isBlank(longitudeValue)) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Please select the complaint location on the map."
            );
            return;
        }

        Double latitude;
        Double longitude;

        try {

            latitude = Double.parseDouble(latitudeValue.trim());
            longitude = Double.parseDouble(longitudeValue.trim());

            if (!Double.isFinite(latitude)
                    || !Double.isFinite(longitude)
                    || latitude < -90
                    || latitude > 90
                    || longitude < -180
                    || longitude > 180) {

                throw new NumberFormatException(
                    "Invalid geographical coordinates."
                );
            }

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid complaint location coordinates."
            );
            return;
        }

        String photoPath = null;
        Path savedPhoto = null;

        try {

            // 7. Handle photo upload
            Part photo = request.getPart("photo");

            if (photo != null && photo.getSize() > 0) {

                String contentType = photo.getContentType();
                String extension;

                if ("image/jpeg".equalsIgnoreCase(contentType)) {
                    extension = ".jpg";

                } else if ("image/png".equalsIgnoreCase(contentType)) {
                    extension = ".png";

                } else if ("image/webp".equalsIgnoreCase(contentType)) {
                    extension = ".webp";

                } else {

                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Only JPG, PNG and WEBP images are allowed."
                    );
                    return;
                }

                String uploadDirectory =
                    getServletContext().getRealPath("/uploads");

                if (uploadDirectory == null) {

                    throw new IOException(
                        "Upload directory could not be located."
                    );
                }

                Path directory = Paths.get(uploadDirectory);

                Files.createDirectories(directory);

                String fileName =
                    UUID.randomUUID().toString() + extension;

                savedPhoto = directory.resolve(fileName);

                photo.write(savedPhoto.toString());

                photoPath = "uploads/" + fileName;
            }

            // 8. Create complaint
            Complaint complaint = new Complaint(
                citizenName.trim(),
                category.trim(),
                issueTitle.trim(),
                description.trim(),
                address.trim(),
                city.trim(),
                pincode.trim(),
                photoPath,
                latitude,
                longitude
            );

            // Associate complaint with logged-in citizen
            complaint.setCitizenId(citizenId);

            // 9. Save complaint in MySQL
            long complaintId =
                    complaintDAO.addComplaint(complaint);

            if (complaintId > 0) {

                String displayId =
                    String.format("VG-%05d", complaintId);

                request.setAttribute(
                    "complaintId",
                    displayId
                );

                request.getRequestDispatcher(
                    "/complaint-success.jsp"
                ).forward(request, response);

            } else {

                if (savedPhoto != null) {
                    Files.deleteIfExists(savedPhoto);
                }

                response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Complaint could not be saved. Please try again."
                );
            }

        } catch (java.sql.SQLException e) {

            // Remove uploaded photo if database insertion fails
            if (savedPhoto != null) {
                Files.deleteIfExists(savedPhoto);
            }

            getServletContext().log(
                "Database error while submitting complaint.",
                e
            );

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "A database error occurred. Please try again later."
            );
        }
    }

    // 10. Check whether a string is empty or null
    private boolean isBlank(String value) {

        return value == null || value.trim().isEmpty();
    }
}