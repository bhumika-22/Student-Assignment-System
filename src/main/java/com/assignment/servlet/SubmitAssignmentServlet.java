package com.assignment.servlet;

import com.assignment.dao.SubmissionDAO;
import com.assignment.model.Submission;
import com.assignment.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Servlet handling file uploads for assignment submission by students.
 */
@WebServlet(name = "SubmitAssignmentServlet", urlPatterns = {"/student/submit-assignment"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2 MB memory buffer
    maxFileSize = 1024 * 1024 * 25,       // 25 MB max per file
    maxRequestSize = 1024 * 1024 * 50     // 50 MB max request
)
public class SubmitAssignmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SubmissionDAO submissionDAO;

    @Override
    public void init() {
        this.submissionDAO = new SubmissionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User student = (User) session.getAttribute("user");

        String assignmentIdStr = request.getParameter("assignmentId");
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=invalid_assignment");
            return;
        }

        int assignmentId;
        try {
            assignmentId = Integer.parseInt(assignmentIdStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=invalid_assignment");
            return;
        }

        Part filePart = request.getPart("solutionFile");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=empty_file");
            return;
        }

        String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (submittedFileName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=empty_file");
            return;
        }

        // Determine destination upload directory
        String uploadDir = getServletContext().getRealPath("/uploads");
        if (uploadDir == null) {
            uploadDir = System.getProperty("java.io.tmpdir") + File.separator + "StudentAssignmentUploads";
        }
        File uploadFolder = new File(uploadDir);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        // Sanitize filename & create unique stored filename
        String safeName = submittedFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String uniqueFilename = "sub_a" + assignmentId + "_s" + student.getId() + "_" + System.currentTimeMillis() + "_" + safeName;
        File targetFile = new File(uploadFolder, uniqueFilename);

        // Write uploaded file to disk
        filePart.write(targetFile.getAbsolutePath());

        // Save submission record in database
        Submission submission = new Submission();
        submission.setAssignmentId(assignmentId);
        submission.setStudentId(student.getId());
        submission.setOriginalFilename(submittedFileName);
        submission.setStoredFilename(uniqueFilename);
        submission.setFilePath(targetFile.getAbsolutePath());
        submission.setFileSize(filePart.getSize());

        boolean success = submissionDAO.saveOrUpdateSubmission(submission);
        if (success) {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?submitted=true");
        } else {
            response.sendRedirect(request.getContextPath() + "/student/dashboard?error=db_error");
        }
    }
}
