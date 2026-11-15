package com.viswagrid.model;

public class Citizen {

    private int citizenId;
    private String fullName;
    private String email;
    private String mobile;
    private String passwordHash;

    // Default constructor
    public Citizen() {
    }

    // Constructor for citizen registration
    public Citizen(String fullName, String email,
                   String mobile, String passwordHash) {

        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.passwordHash = passwordHash;
    }

    // Constructor including citizen ID
    public Citizen(int citizenId, String fullName,
                   String email, String mobile,
                   String passwordHash) {

        this.citizenId = citizenId;
        this.fullName = fullName;
        this.email = email;
        this.mobile = mobile;
        this.passwordHash = passwordHash;
    }

    public int getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(int citizenId) {
        this.citizenId = citizenId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}