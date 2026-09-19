package com.yournamemart.model;

import java.sql.Timestamp;

public class User {
    private long id;
    private String email;
    private String passwordHash;
    private String fullName;
    private String role; // BUYER, SELLER, ADMIN
    private Timestamp createdAt;
    private boolean active;

    public User() {}

    public User(String email, String passwordHash, String fullName, String role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isAdmin() { return "ADMIN".equals(role); }
    public boolean isSeller() { return "SELLER".equals(role); }
    public boolean isBuyer() { return "BUYER".equals(role); }
}
