package com.assignment.servlet;

import com.assignment.dao.AssignmentDAO;
import com.assignment.dao.SubmissionDAO;
import com.assignment.model.Assignment;
import com.assignment.model.Submission;
import com.assignment.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet for downloading and previewing submitted assignment files.
 */
@WebServlet(name = "FileDownloadServlet", urlPatterns = {"/download"})
public class FileDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SubmissionDAO submissionDAO;
    private AssignmentDAO assignmentDAO;

    @Override
    public void init() {
        this.submissionDAO = new SubmissionDAO();
        this.assignmentDAO = new AssignmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");

        String submissionIdStr = request.getParameter("submissionId");
        if (submissionIdStr == null || submissionIdStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing submission ID");
            return;
        }

        int submissionId;
        try {
            submissionId = Integer.parseInt(submissionIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid submission ID");
            return;
        }

        Submission submission = submissionDAO.getSubmissionById(submissionId);
        if (submission == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Submission not found");
            return;
        }

        // Verify authorization: must be the student who submitted OR the teacher of the assignment
        if (user.isStudent() && submission.getStudentId() != user.getId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to this submission");
            return;
        }

        if (user.isTeacher()) {
            Assignment assignment = assignmentDAO.getAssignmentById(submission.getAssignmentId());
            if (assignment == null || assignment.getTeacherId() != user.getId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied to this submission");
                return;
            }
        }

        File file = new File(submission.getFilePath());
        if (!file.exists()) {
            // Also check relative to webapp uploads folder in case server restarted with different path
            String uploadDir = getServletContext().getRealPath("/uploads");
            if (uploadDir != null) {
                File altFile = new File(uploadDir, submission.getStoredFilename());
                if (altFile.exists()) {
                    file = altFile;
                }
            }
        }

        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Solution file not found on disk");
            return;
        }

        String mimeType = getServletContext().getMimeType(file.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLengthLong(file.length());

        String safeOriginalName = submission.getOriginalFilename().replace("\"", "");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + safeOriginalName + "\"");

        try (FileInputStream inStream = new FileInputStream(file);
             OutputStream outStream = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            outStream.flush();
        }
    }
}
