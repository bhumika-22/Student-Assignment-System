package com.assignment.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing an Assignment created by a Teacher.
 */
public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String title;
    private String subject;
    private String description;
    private Date dueDate;
    private int teacherId;
    private String teacherName; // For student view
    private int submissionCount; // For teacher dashboard
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Student-specific submission status fields (populated when viewed by a student)
    private Integer submissionId;
    private String submissionStatus; // "SUBMITTED", "EVALUATED", or null
    private Integer marks;
    private String feedback;
    private Timestamp submittedAt;
    private String originalFilename;
    private String storedFilename;

    public Assignment() {
    }

    public Assignment(int id, String title, String subject, String description, Date dueDate, int teacherId) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.description = description;
        this.dueDate = dueDate;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSubmissionCount() {
        return submissionCount;
    }

    public void setSubmissionCount(int submissionCount) {
        this.submissionCount = submissionCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(String submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public boolean isSubmitted() {
        return submissionStatus != null && !submissionStatus.isEmpty();
    }

    public boolean isEvaluated() {
        return "EVALUATED".equalsIgnoreCase(this.submissionStatus);
    }

    public boolean isPastDue() {
        if (dueDate == null) return false;
        long now = System.currentTimeMillis();
        // due date end of day
        return dueDate.getTime() + 86400000L < now;
    }
}
