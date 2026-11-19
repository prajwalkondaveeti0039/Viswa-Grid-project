package com.viswagrid.model;

public class Complaint {
	
	private Integer citizenId;
    private long complaintId;
    private String citizenName;
    private String category;
    private String issueTitle;
    private String description;
    private String address;
    private String city;
    private String pincode;
    private String photoPath;
    private String status;
    private Integer departmentId;
    private String departmentName;

    // New location fields
    private Double latitude;
    private Double longitude;

    // Default constructor
    public Complaint() {
    }

    // Constructor for submitting a new complaint
    public Complaint(String citizenName, String category,
            String issueTitle, String description,
            String address, String city, String pincode,
            String photoPath) {

        this.citizenName = citizenName;
        this.category = category;
        this.issueTitle = issueTitle;
        this.description = description;
        this.address = address;
        this.city = city;
        this.pincode = pincode;
        this.photoPath = photoPath;
        this.status = "SUBMITTED";
    }

    // Constructor including complaint location
    public Complaint(String citizenName, String category,
            String issueTitle, String description,
            String address, String city, String pincode,
            String photoPath, Double latitude, Double longitude) {

        this(citizenName, category, issueTitle, description,
                address, city, pincode, photoPath);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(long complaintId) {
        this.complaintId = complaintId;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Latitude getter and setter
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    // Longitude getter and setter
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Integer getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Integer citizenId) {
        this.citizenId = citizenId;
    }
} 