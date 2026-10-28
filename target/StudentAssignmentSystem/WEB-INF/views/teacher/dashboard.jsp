<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Teacher Dashboard - EduAssign</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="dashboard-layout">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <a href="${pageContext.request.contextPath}/" class="sidebar-brand">
                <span>📚</span>
                <span>EduAssign</span>
            </a>

            <nav class="sidebar-nav">
                <a href="${pageContext.request.contextPath}/teacher/dashboard" class="nav-item active">
                    <span>📊</span>
                    <span>Dashboard</span>
                </a>
                <a href="javascript:void(0)" onclick="openCreateModal()" class="nav-item">
                    <span>➕</span>
                    <span>New Assignment</span>
                </a>
            </nav>

            <div class="sidebar-user">
                <div class="user-info">
                    <span class="user-name">${sessionScope.userName}</span>
                    <span class="user-role">Teacher / Instructor</span>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline" title="Log Out" style="color: #cbd5e1; border-color: rgba(255,255,255,0.2);">
                    🚪
                </a>
            </div>
        </aside>

        <!-- Main Content Area -->
        <div class="main-content">
            <header class="top-navbar">
                <div class="page-title">Teacher Dashboard</div>
                <div style="display: flex; gap: 1rem; align-items: center;">
                    <button type="button" class="btn btn-primary" onclick="openCreateModal()">
                        <span>➕ Create New Assignment</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">
                        Log Out
                    </a>
                </div>
            </header>

            <main class="content-body">
                <!-- Notifications -->
                <c:if test="${param.created eq 'true'}">
                    <div class="alert alert-success" style="margin-bottom: 1.5rem;">
                        <span>✅</span>
                        <div>Assignment created and published successfully! Students can now submit their solutions.</div>
                    </div>
                </c:if>
                <c:if test="${param.deleted eq 'true'}">
                    <div class="alert alert-info" style="margin-bottom: 1.5rem;">
                        <span>🗑️</span>
                        <div>Assignment and associated records were deleted.</div>
                    </div>
                </c:if>
                <c:if test="${param.error eq 'missing_fields'}">
                    <div class="alert alert-danger" style="margin-bottom: 1.5rem;">
                        <span>⚠️</span>
                        <div>Please fill in all assignment fields (Title, Subject, Due Date, and Description).</div>
                    </div>
                </c:if>

                <!-- Stats Overview Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--primary-light); color: var(--primary);">📝</div>
                        <div class="stat-details">
                            <h4>Total Assignments</h4>
                            <div class="stat-number">${totalAssignments}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--info-bg); color: var(--info);">📥</div>
                        <div class="stat-details">
                            <h4>Submissions Received</h4>
                            <div class="stat-number">${totalSubmissions}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--warning-bg); color: var(--warning);">⏳</div>
                        <div class="stat-details">
                            <h4>Pending Evaluations</h4>
                            <div class="stat-number">${pendingEvaluations}</div>
                        </div>
                    </div>
                </div>

                <!-- Assignments Table -->
                <div class="table-card">
                    <div class="table-header">
                        <h3>Published Assignments</h3>
                        <span style="font-size: 0.875rem; color: var(--text-secondary);">Manage deadlines and grade submissions</span>
                    </div>

                    <div class="table-responsive">
                        <c:choose>
                            <c:when test="${empty assignments}">
                                <div style="text-align: center; padding: 3rem 1rem;">
                                    <div style="font-size: 3rem; margin-bottom: 0.5rem;">📋</div>
                                    <h4 style="font-size: 1.1rem; color: var(--text-primary); margin-bottom: 0.5rem;">No assignments published yet</h4>
                                    <p style="color: var(--text-muted); margin-bottom: 1.25rem;">Create your first coursework assignment to get started receiving submissions.</p>
                                    <button type="button" class="btn btn-primary" onclick="openCreateModal()">
                                        ➕ Create Assignment Now
                                    </button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Subject & Title</th>
                                            <th>Due Date</th>
                                            <th>Submissions</th>
                                            <th>Created Date</th>
                                            <th style="text-align: right;">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="a" items="${assignments}">
                                            <tr>
                                                <td>
                                                    <span class="assignment-subject" style="margin-bottom: 0.35rem;">${a.subject}</span>
                                                    <div style="font-weight: 700; color: var(--text-primary);">${a.title}</div>
                                                    <div style="font-size: 0.8rem; color: var(--text-secondary); max-width: 400px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">
                                                        ${a.description}
                                                    </div>
                                                </td>
                                                <td>
                                                    <div style="font-weight: 600;">${a.dueDate}</div>
                                                    <c:choose>
                                                        <c:when test="${a.pastDue}">
                                                            <span class="badge badge-overdue" style="font-size: 0.7rem;">Expired</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-pending" style="font-size: 0.7rem;">Active</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <span class="badge badge-submitted">
                                                        📥 ${a.submissionCount} Submissions
                                                    </span>
                                                </td>
                                                <td style="font-size: 0.85rem; color: var(--text-muted);">
                                                    <fmt:formatDate value="${a.createdAt}" pattern="dd MMM yyyy" />
                                                </td>
                                                <td style="text-align: right; white-space: nowrap;">
                                                    <a href="${pageContext.request.contextPath}/teacher/submissions?assignmentId=${a.id}" 
                                                       class="btn btn-primary btn-sm">
                                                        Review (${a.submissionCount})
                                                    </a>
                                                    <form action="${pageContext.request.contextPath}/teacher/delete-assignment" method="POST" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this assignment and all its submissions?');">
                                                        <input type="hidden" name="assignmentId" value="${a.id}">
                                                        <button type="submit" class="btn btn-outline btn-sm" style="color: var(--danger); border-color: rgba(239,68,68,0.3); margin-left: 0.35rem;">
                                                            🗑️
                                                        </button>
                                                    </form>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Create Assignment Modal -->
    <div class="modal-overlay" id="createModal">
        <div class="modal-box">
            <div class="modal-header">
                <h3>➕ Create New Assignment</h3>
                <button type="button" class="modal-close" onclick="closeCreateModal()">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/teacher/create-assignment" method="POST">
                <div class="modal-body auth-form">
                    <div class="form-group">
                        <label for="subject">Course / Subject Name</label>
                        <input type="text" id="subject" name="subject" placeholder="e.g. Data Structures & Algorithms" required>
                    </div>

                    <div class="form-group">
                        <label for="title">Assignment Title</label>
                        <input type="text" id="title" name="title" placeholder="e.g. Assignment 1 - Implementation & Analysis" required>
                    </div>

                    <div class="form-group">
                        <label for="dueDate">Submission Due Date</label>
                        <input type="date" id="dueDate" name="dueDate" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Assignment Description & Guidelines</label>
                        <textarea id="description" name="description" rows="4" placeholder="Detail the requirements, format (PDF/DOCX), and submission instructions..." required></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeCreateModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Publish Assignment &rarr;</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openCreateModal() {
            document.getElementById('createModal').classList.add('active');
            // Set minimum due date as today
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('dueDate').setAttribute('min', today);
        }
        function closeCreateModal() {
            document.getElementById('createModal').classList.remove('active');
        }
        // Close modal when clicking outside
        document.getElementById('createModal').addEventListener('click', function(e) {
            if (e.target === this) closeCreateModal();
        });
    </script>
</body>
</html>
