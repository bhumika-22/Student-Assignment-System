<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Log In - Student Assignment Management System</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-body">
    <div class="auth-wrapper">
        <!-- Auth Header / Logo -->
        <div class="auth-header">
            <a href="${pageContext.request.contextPath}/" class="brand-logo justify-center">
                <span class="logo-icon">📚</span>
                <span class="logo-text">EduAssign</span>
            </a>
            <h2>Welcome Back</h2>
            <p>Log in to access your assignments, evaluations, and submissions</p>
        </div>

        <!-- Auth Card -->
        <div class="auth-card">
            <!-- Feedback Messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    <span>⚠️</span>
                    <div>${error}</div>
                </div>
            </c:if>

            <c:if test="${param.registered eq 'true'}">
                <div class="alert alert-success">
                    <span>✅</span>
                    <div>Registration successful! You can now log in with your credentials.</div>
                </div>
            </c:if>

            <c:if test="${param.logout eq 'true'}">
                <div class="alert alert-info">
                    <span>ℹ️</span>
                    <div>You have been safely logged out.</div>
                </div>
            </c:if>

            <c:if test="${param.sessionExpired eq 'true'}">
                <div class="alert alert-danger">
                    <span>⏱️</span>
                    <div>Your session has expired. Please log in again.</div>
                </div>
            </c:if>

            <!-- Login Form -->
            <form action="${pageContext.request.contextPath}/login" method="POST" class="auth-form" id="loginForm">
                <div class="form-group">
                    <label for="email">Email Address</label>
                    <input type="email" id="email" name="email" value="${email}" 
                           placeholder="e.g. name@college.edu" required autocomplete="email" autofocus>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" 
                           placeholder="Enter your secure password" required autocomplete="current-password">
                </div>

                <button type="submit" class="btn btn-primary btn-block btn-large">
                    Sign In &rarr;
                </button>
            </form>

            <!-- Quick Demo Credentials helper for Viva / Demo -->
            <div class="demo-box">
                <p class="demo-title">💡 <strong>Quick Demo Credentials (Click to fill):</strong></p>
                <div class="demo-buttons">
                    <button type="button" class="btn btn-sm btn-outline" onclick="fillCredentials('teacher@college.edu', 'teacher123')">
                        👩‍🏫 Fill Teacher
                    </button>
                    <button type="button" class="btn btn-sm btn-outline" onclick="fillCredentials('student@college.edu', 'student123')">
                        👨‍🎓 Fill Student
                    </button>
                </div>
            </div>

            <div class="auth-footer">
                Don't have an account yet? 
                <a href="${pageContext.request.contextPath}/register.jsp">Create Student Account</a>
            </div>
        </div>
    </div>

    <script>
        function fillCredentials(email, pwd) {
            document.getElementById('email').value = email;
            document.getElementById('password').value = pwd;
        }
    </script>
</body>
</html>
