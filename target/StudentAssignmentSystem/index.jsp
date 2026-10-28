<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    // If user is already logged in, redirect them to their respective dashboard
    if (session.getAttribute("user") != null) {
        String role = (String) session.getAttribute("role");
        if ("TEACHER".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
            return;
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
            return;
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Assignment Management System</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="landing-body">
    <!-- Navigation Bar -->
    <header class="navbar">
        <div class="nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="logo-icon">📚</span>
                <span class="logo-text">EduAssign</span>
            </a>
        </div>
    </header>

    <!-- Hero Section -->
    <main class="hero-section">
        <div class="hero-container">
            <h1 class="hero-title">Student Assignment<br><span class="gradient-text">Management System</span></h1>
            <p class="hero-subtitle">
                A seamless digital portal for college teachers to publish, evaluate, and grade assignments, 
                and for students to submit coursework and track progress in real-time.
            </p>

            <div class="hero-cta-group">
                <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-primary btn-large">
                    Get Started &rarr;
                </a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-secondary btn-large">
                    Student Registration
                </a>
            </div>

            <!-- Role Cards Preview -->
            <div class="features-grid">
                <div class="feature-card">
                    <div class="card-icon">👩‍🏫</div>
                    <h3>Teacher Portal</h3>
                    <p>Post assignments with due dates, review PDF/DOCX submissions, evaluate work, and assign marks & feedback.</p>
                </div>
                <div class="feature-card">
                    <div class="card-icon">👨‍🎓</div>
                    <h3>Student Portal</h3>
                    <p>View active assignments, track pending deadlines, upload solution files, and view evaluated marks instantly.</p>
                </div>
                <div class="feature-card">
                    <div class="card-icon">🔒</div>
                    <h3>Secure & Reliable</h3>
                    <p>Session-based authentication, role-based authorization, BCrypt password hashing, and safe file uploads.</p>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer -->
    <footer class="footer">
        <div class="footer-container">
            <p>&copy; 2026 Student Assignment Management System. All rights reserved.</p>
        </div>
    </footer>
</body>
</html>
