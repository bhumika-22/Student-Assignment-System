<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Student Assignment Management System</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-wrapper">
        <div class="auth-header">
            <a href="${pageContext.request.contextPath}/" class="brand-logo justify-center">
                <span class="logo-icon">📚</span>
                <span class="logo-text">EduAssign</span>
            </a>
            <h2>Create Your Account</h2>
            <p>Join the college portal to manage and submit your assignments</p>
        </div>

        <div class="auth-card">
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <span>⚠️</span>
                    <div>${error}</div>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" class="auth-form" id="registerForm">
                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text" id="fullName" name="fullName" value="${fullName}" 
                           placeholder="e.g. John Doe" required autofocus>
                </div>

                <div class="form-group">
                    <label for="email">College Email Address</label>
                    <input type="email" id="email" name="email" value="${email}" 
                           placeholder="e.g. john@college.edu" required autocomplete="email">
                </div>

                <div class="form-group">
                    <label for="role">Select Your Role</label>
                    <select id="role" name="role" required class="form-select">
                        <option value="STUDENT" ${role eq 'STUDENT' ? 'selected' : ''}>Student</option>
                        <option value="TEACHER" ${role eq 'TEACHER' ? 'selected' : ''}>Teacher / Instructor</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="password">Password (Minimum 6 characters)</label>
                    <input type="password" id="password" name="password" 
                           placeholder="Choose a strong password" required minlength="6" autocomplete="new-password">
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="Re-type your password" required minlength="6" autocomplete="new-password">
                </div>

                <button type="submit" class="btn btn-primary btn-block btn-large">
                    Create Account &rarr;
                </button>
            </form>

            <div class="auth-footer">
                Already registered? 
                <a href="${pageContext.request.contextPath}/login.jsp">Sign in here</a>
            </div>
        </div>
    </div>
</body>
</html>
