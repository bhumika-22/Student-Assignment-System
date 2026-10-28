<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submissions Review - ${assignment.title}</title>
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
                <a href="${pageContext.request.contextPath}/teacher/dashboard" class="nav-item">
                    <span>📊</span>
                    <span>Dashboard</span>
                </a>
                <a href="javascript:void(0)" class="nav-item active">
                    <span>📝</span>
                    <span>Review Submissions</span>
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
                <div style="display: flex; align-items: center; gap: 1rem;">
                    <a href="${pageContext.request.contextPath}/teacher/dashboard" class="btn btn-outline btn-sm">
                        &larr; Back to Dashboard
                    </a>
                    <div class="page-title">Review Submissions</div>
                </div>
                <div style="display: flex; gap: 1rem; align-items: center;">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">
                        Log Out
                    </a>
                </div>
            </header>

            <main class="content-body">
                <!-- Notifications -->
                <c:if test="${param.graded eq 'true'}">
                    <div class="alert alert-success" style="margin-bottom: 1.5rem;">
                        <span>✅</span>
                        <div>Student submission was successfully evaluated and marks have been recorded!</div>
                    </div>
                </c:if>

                <!-- Assignment Overview Card -->
                <div class="stat-card" style="margin-bottom: 1.5rem; display: block;">
                    <div style="display: flex; justify-content: space-between; align-items: flex-start; flex-wrap: wrap; gap: 1rem;">
                        <div>
                            <span class="assignment-subject" style="margin-bottom: 0.5rem;">${assignment.subject}</span>
                            <h2 style="font-size: 1.4rem; font-weight: 800; color: var(--text-primary); margin-bottom: 0.5rem;">
                                ${assignment.title}
                            </h2>
                            <p style="color: var(--text-secondary); font-size: 0.925rem; max-width: 800px;">
                                ${assignment.description}
                            </p>
                        </div>
                        <div style="text-align: right; font-size: 0.9rem;">
                            <div style="color: var(--text-muted); margin-bottom: 0.25rem;">Due Date:</div>
                            <div style="font-weight: 700; color: var(--text-primary); font-size: 1rem;">
                                ${assignment.dueDate}
                            </div>
                            <div style="margin-top: 0.5rem;">
                                <span class="badge badge-submitted">
                                    📥 ${submissions.size()} Submissions Total
                                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Submissions Table Card -->
                <div class="table-card">
                    <div class="table-header">
                        <h3>Student Submissions</h3>
                        <span style="font-size: 0.875rem; color: var(--text-secondary);">
                            Review uploaded documents, assign scores and provide feedback
                        </span>
                    </div>

                    <div class="table-responsive">
                        <c:choose>
                            <c:when test="${empty submissions}">
                                <div style="text-align: center; padding: 3rem 1rem;">
                                    <div style="font-size: 3rem; margin-bottom: 0.5rem;">📭</div>
                                    <h4 style="font-size: 1.1rem; color: var(--text-primary); margin-bottom: 0.5rem;">
                                        No student has submitted work for this assignment yet.
                                    </h4>
                                    <p style="color: var(--text-muted);">
                                        Students will appear here once they upload their solution files from the student portal.
                                    </p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Student</th>
                                            <th>Submitted At</th>
                                            <th>Attached Solution File</th>
                                            <th>Evaluation Status</th>
                                            <th>Marks & Feedback</th>
                                            <th style="text-align: right;">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="s" items="${submissions}">
                                            <tr>
                                                <td>
                                                    <div style="font-weight: 700; color: var(--text-primary);">${s.studentName}</div>
                                                    <div style="font-size: 0.8rem; color: var(--text-muted);">${s.studentEmail}</div>
                                                </td>
                                                <td style="font-size: 0.85rem; color: var(--text-secondary);">
                                                    <fmt:formatDate value="${s.submittedAt}" pattern="dd MMM yyyy, hh:mm a" />
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/download?submissionId=${s.id}" 
                                                       class="btn btn-outline btn-sm" style="font-size: 0.8rem; padding: 0.4rem 0.75rem;">
                                                        📄 ${s.originalFilename} 
                                                        <span style="color: var(--text-muted); font-size: 0.75rem;">(${s.formattedFileSize})</span>
                                                    </a>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${s.evaluated}">
                                                            <span class="badge badge-evaluated">✅ Evaluated</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-pending">⏳ Awaiting Review</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${s.evaluated}">
                                                            <div style="font-weight: 800; color: var(--success); font-size: 1.05rem;">
                                                                ${s.marks} / 100
                                                            </div>
                                                            <c:if test="${not empty s.feedback}">
                                                                <div style="font-size: 0.8rem; color: var(--text-secondary); max-width: 250px;">
                                                                    "${s.feedback}"
                                                                </div>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span style="color: var(--text-muted); font-size: 0.85rem;">Not graded</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td style="text-align: right;">
                                                    <button type="button" class="btn btn-primary btn-sm" 
                                                            onclick="openGradeModal('${s.id}', '${s.studentName}', '${s.marks != null ? s.marks : ''}', '${s.feedback != null ? s.feedback : ''}')">
                                                        <c:choose>
                                                            <c:when test="${s.evaluated}">✏️ Re-Grade</c:when>
                                                            <c:otherwise>📝 Grade Work</c:otherwise>
                                                        </c:choose>
                                                    </button>
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

    <!-- Grade Submission Modal -->
    <div class="modal-overlay" id="gradeModal">
        <div class="modal-box">
            <div class="modal-header">
                <h3>📝 Evaluate Student Work</h3>
                <button type="button" class="modal-close" onclick="closeGradeModal()">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/teacher/grade-submission" method="POST">
                <input type="hidden" name="assignmentId" value="${assignment.id}">
                <input type="hidden" name="submissionId" id="modalSubmissionId" value="">

                <div class="modal-body auth-form">
                    <div style="background-color: var(--primary-light); padding: 0.75rem 1rem; border-radius: var(--radius-md); margin-bottom: 0.5rem;">
                        <span style="font-size: 0.8rem; color: var(--primary); text-transform: uppercase; font-weight: 700;">Student:</span>
                        <div id="modalStudentName" style="font-weight: 700; color: var(--text-primary); font-size: 1.05rem;">Student Name</div>
                    </div>

                    <div class="form-group">
                        <label for="marks">Award Marks (0 to 100)</label>
                        <input type="number" id="marks" name="marks" min="0" max="100" placeholder="e.g. 85" required>
                    </div>

                    <div class="form-group">
                        <label for="feedback">Teacher Feedback & Comments</label>
                        <textarea id="feedback" name="feedback" rows="4" placeholder="Provide constructive remarks, suggestions for improvement, or praise for good work..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeGradeModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Evaluation &rarr;</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openGradeModal(submissionId, studentName, marks, feedback) {
            document.getElementById('modalSubmissionId').value = submissionId;
            document.getElementById('modalStudentName').textContent = studentName;
            document.getElementById('marks').value = marks;
            document.getElementById('feedback').value = feedback;
            document.getElementById('gradeModal').classList.add('active');
        }
        function closeGradeModal() {
            document.getElementById('gradeModal').classList.remove('active');
        }
        document.getElementById('gradeModal').addEventListener('click', function(e) {
            if (e.target === this) closeGradeModal();
        });
    </script>
</body>
</html>
