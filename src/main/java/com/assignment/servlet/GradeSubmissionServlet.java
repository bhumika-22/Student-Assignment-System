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

import java.io.IOException;

/**
 * Servlet for teachers to evaluate a submission by entering marks and feedback.
 */
@WebServlet(name = "GradeSubmissionServlet", urlPatterns = {"/teacher/grade-submission"})
public class GradeSubmissionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AssignmentDAO assignmentDAO;
    private SubmissionDAO submissionDAO;

    @Override
    public void init() {
        this.assignmentDAO = new AssignmentDAO();
        this.submissionDAO = new SubmissionDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User teacher = (User) session.getAttribute("user");

        String submissionIdStr = request.getParameter("submissionId");
        String assignmentIdStr = request.getParameter("assignmentId");
        String marksStr = request.getParameter("marks");
        String feedback = request.getParameter("feedback");

        if (submissionIdStr == null || assignmentIdStr == null || marksStr == null) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
            return;
        }

        try {
            int submissionId = Integer.parseInt(submissionIdStr.trim());
            int assignmentId = Integer.parseInt(assignmentIdStr.trim());
            int marks = Integer.parseInt(marksStr.trim());

            // Validate that assignment belongs to this teacher
            Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);
            if (assignment == null || assignment.getTeacherId() != teacher.getId()) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                return;
            }

            boolean success = submissionDAO.gradeSubmission(submissionId, marks, feedback);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/teacher/submissions?assignmentId=" + assignmentId + "&graded=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/teacher/submissions?assignmentId=" + assignmentId + "&error=grade_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard?error=invalid_marks");
        }
    }
}
