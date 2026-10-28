<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Portal - EduAssign</title>
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
                <a href="${pageContext.request.contextPath}/student/dashboard" class="nav-item active">
                    <span>📖</span>
                    <span>My Assignments</span>
                </a>
            </nav>

            <div class="sidebar-user">
                <div class="user-info">
                    <span class="user-name">${sessionScope.userName}</span>
                    <span class="user-role">Student</span>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline" title="Log Out" style="color: #cbd5e1; border-color: rgba(255,255,255,0.2);">
                    🚪
                </a>
            </div>
        </aside>

        <!-- Main Content Area -->
        <div class="main-content">
            <header class="top-navbar">
                <div class="page-title">Student Assignment Portal</div>
                <div style="display: flex; gap: 1rem; align-items: center;">
                    <span style="font-size: 0.9rem; color: var(--text-secondary);">
                        Welcome, <strong>${sessionScope.userName}</strong>
                    </span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline btn-sm">
                        Log Out
                    </a>
                </div>
            </header>

            <main class="content-body">
                <!-- Notifications -->
                <c:if test="${param.submitted eq 'true'}">
                    <div class="alert alert-success" style="margin-bottom: 1.5rem;">
                        <span>✅</span>
                        <div>Assignment solution uploaded and submitted successfully! Your instructor has been notified for evaluation.</div>
                    </div>
                </c:if>
                <c:if test="${param.error eq 'empty_file'}">
                    <div class="alert alert-danger" style="margin-bottom: 1.5rem;">
                        <span>⚠️</span>
                        <div>Please select a valid document or PDF file to upload.</div>
                    </div>
                </c:if>

                <!-- Stats Overview Cards -->
                <div class="stats-grid">
                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--primary-light); color: var(--primary);">📚</div>
                        <div class="stat-details">
                            <h4>Total Coursework</h4>
                            <div class="stat-number">${totalAvailable}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--warning-bg); color: var(--warning);">⏳</div>
                        <div class="stat-details">
                            <h4>Pending Deadlines</h4>
                            <div class="stat-number">${pendingCount}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--info-bg); color: var(--info);">📤</div>
                        <div class="stat-details">
                            <h4>Submitted</h4>
                            <div class="stat-number">${submittedCount}</div>
                        </div>
                    </div>

                    <div class="stat-card">
                        <div class="stat-icon" style="background-color: var(--success-bg); color: var(--success);">🎯</div>
                        <div class="stat-details">
                            <h4>Evaluated & Graded</h4>
                            <div class="stat-number">${evaluatedCount}</div>
                        </div>
                    </div>
                </div>

                <!-- Assignment Cards Section -->
                <div style="margin-bottom: 1.5rem; display: flex; justify-content: space-between; align-items: center;">
                    <div>
                        <h2 style="font-size: 1.25rem; font-weight: 800; color: var(--text-primary);">Your Assignments</h2>
                        <p style="font-size: 0.875rem; color: var(--text-secondary);">Review guidelines, submit work before deadline, and check teacher marks</p>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty assignments}">
                        <div class="table-card" style="text-align: center; padding: 4rem 1rem;">
                            <div style="font-size: 3.5rem; margin-bottom: 0.75rem;">🎉</div>
                            <h3 style="font-size: 1.25rem; color: var(--text-primary); margin-bottom: 0.5rem;">All caught up!</h3>
                            <p style="color: var(--text-muted);">There are no pending assignments posted by your teachers right now.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="cards-grid">
                            <c:forEach var="a" items="${assignments}">
                                <div class="assignment-card">
                                    <div>
                                        <div class="assignment-card-header">
                                            <span class="assignment-subject">${a.subject}</span>
                                            <c:choose>
                                                <c:when test="${a.evaluated}">
                                                    <span class="badge badge-evaluated">✅ Graded</span>
                                                </c:when>
                                                <c:when test="${a.submitted}">
                                                    <span class="badge badge-submitted">📤 Submitted</span>
                                                </c:when>
                                                <c:when test="${a.pastDue}">
                                                    <span class="badge badge-overdue">⚠️ Past Due</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-pending">⏳ Due Soon</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <h3 class="assignment-title">${a.title}</h3>
                                        <p class="assignment-desc">${a.description}</p>
                                    </div>

                                    <div>
                                        <!-- Evaluation Feedback Box if evaluated -->
                                        <c:if test="${a.evaluated}">
                                            <div class="feedback-banner">
                                                <div style="display: flex; justify-content: space-between; align-items: baseline;">
                                                    <span style="font-weight: 700; color: #065f46; font-size: 0.8rem; text-transform: uppercase;">
                                                        Awarded Score:
                                                    </span>
                                                    <div class="feedback-score">${a.marks} / 100</div>
                                                </div>
                                                <c:if test="${not empty a.feedback}">
                                                    <div class="feedback-comment">
                                                        "${a.feedback}"
                                                    </div>
                                                </c:if>
                                            </div>
                                        </c:if>

                                        <div class="assignment-meta">
                                            <div class="assignment-meta-row">
                                                <span>Instructor:</span>
                                                <strong>${a.teacherName}</strong>
                                            </div>
                                            <div class="assignment-meta-row">
                                                <span>Deadline:</span>
                                                <strong style="color: ${a.pastDue ? 'var(--danger)' : 'var(--text-primary)'};">
                                                    ${a.dueDate}
                                                </strong>
                                            </div>
                                            <c:if test="${a.submitted}">
                                                <div class="assignment-meta-row">
                                                    <span>Your File:</span>
                                                    <a href="${pageContext.request.contextPath}/download?submissionId=${a.submissionId}" 
                                                       style="font-weight: 600; text-decoration: underline; font-size: 0.8rem;">
                                                        📄 ${a.originalFilename}
                                                    </a>
                                                </div>
                                            </c:if>
                                        </div>

                                        <!-- Submission / Action Buttons -->
                                        <c:choose>
                                            <c:when test="${a.evaluated}">
                                                <div style="display: flex; gap: 0.5rem;">
                                                    <a href="${pageContext.request.contextPath}/download?submissionId=${a.submissionId}" 
                                                       class="btn btn-outline btn-block btn-sm">
                                                        📥 Download Submitted Work
                                                    </a>
                                                </div>
                                            </c:when>
                                            <c:when test="${a.submitted}">
                                                <div style="display: flex; gap: 0.5rem;">
                                                    <a href="${pageContext.request.contextPath}/download?submissionId=${a.submissionId}" 
                                                       class="btn btn-outline btn-sm" style="flex: 1;">
                                                        📄 View
                                                    </a>
                                                    <button type="button" class="btn btn-primary btn-sm" style="flex: 1;"
                                                            onclick="openUploadModal('${a.id}', '${a.title}', '${a.subject}')">
                                                        🔄 Re-upload
                                                    </button>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-primary btn-block"
                                                        onclick="openUploadModal('${a.id}', '${a.title}', '${a.subject}')">
                                                    📤 Submit Assignment &rarr;
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>
        </div>
    </div>

    <!-- Submit Assignment Modal -->
    <div class="modal-overlay" id="uploadModal">
        <div class="modal-box">
            <div class="modal-header">
                <h3>📤 Upload Assignment Solution</h3>
                <button type="button" class="modal-close" onclick="closeUploadModal()">&times;</button>
            </div>
            <form action="${pageContext.request.contextPath}/student/submit-assignment" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="assignmentId" id="modalAssignmentId" value="">

                <div class="modal-body auth-form">
                    <div style="background-color: var(--primary-light); padding: 0.75rem 1rem; border-radius: var(--radius-md); margin-bottom: 0.5rem;">
                        <span id="modalSubject" style="font-size: 0.75rem; color: var(--primary); text-transform: uppercase; font-weight: 700;">Subject</span>
                        <div id="modalAssignmentTitle" style="font-weight: 700; color: var(--text-primary); font-size: 1.05rem;">Title</div>
                    </div>

                    <div class="form-group">
                        <label>Choose File to Upload (PDF, DOCX, ZIP, PPT, Image)</label>
                        <div class="file-input-wrapper">
                            <div style="font-size: 2rem; margin-bottom: 0.5rem;">📁</div>
                            <div id="fileLabel" style="font-weight: 600; color: var(--primary); margin-bottom: 0.25rem;">
                                Click to browse or drag and drop file here
                            </div>
                            <span style="font-size: 0.75rem; color: var(--text-muted);">
                                Maximum file size: 25 MB
                            </span>
                            <input type="file" name="solutionFile" id="solutionFileInput" required onchange="handleFileSelected(this)">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-outline" onclick="closeUploadModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Confirm & Upload Solution &rarr;</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        function openUploadModal(assignmentId, title, subject) {
            document.getElementById('modalAssignmentId').value = assignmentId;
            document.getElementById('modalAssignmentTitle').textContent = title;
            document.getElementById('modalSubject').textContent = subject;
            document.getElementById('fileLabel').textContent = 'Click to browse or drag and drop file here';
            document.getElementById('solutionFileInput').value = '';
            document.getElementById('uploadModal').classList.add('active');
        }
        function closeUploadModal() {
            document.getElementById('uploadModal').classList.remove('active');
        }
        function handleFileSelected(input) {
            if (input.files && input.files[0]) {
                document.getElementById('fileLabel').textContent = 'Selected: ' + input.files[0].name + ' (' + (input.files[0].size / 1024).toFixed(1) + ' KB)';
            }
        }
        document.getElementById('uploadModal').addEventListener('click', function(e) {
            if (e.target === this) closeUploadModal();
        });
    </script>
</body>
</html>
