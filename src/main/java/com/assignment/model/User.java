package com.assignment.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model class representing a User (Student or Teacher) in the system.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String fullName;
    private String email;
    private String password;
    private String role; // "STUDENT" or "TEACHER"
    private Timestamp createdAt;

    public User() {
    }

    public User(int id, String fullName, String email, String password, String role, Timestamp createdAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    public User(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isTeacher() {
        return "TEACHER".equalsIgnoreCase(this.role);
    }

    public boolean isStudent() {
        return "STUDENT".equalsIgnoreCase(this.role);
    }
}
