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
import java.util.List;

/**
 * Servlet for teachers to view and grade student submissions for an assignment.
 */
@WebServlet(name = "TeacherSubmissionsServlet", urlPatterns = {"/teacher/submissions"})
public class TeacherSubmissionsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AssignmentDAO assignmentDAO;
    private SubmissionDAO submissionDAO;

    @Override
    public void init() {
        this.assignmentDAO = new AssignmentDAO();
        this.submissionDAO = new SubmissionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User teacher = (User) session.getAttribute("user");

        String assignmentIdStr = request.getParameter("assignmentId");
        if (assignmentIdStr == null || assignmentIdStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
            return;
        }

        try {
            int assignmentId = Integer.parseInt(assignmentIdStr.trim());
            Assignment assignment = assignmentDAO.getAssignmentById(assignmentId);

            if (assignment == null || assignment.getTeacherId() != teacher.getId()) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                return;
            }

            List<Submission> submissions = submissionDAO.getSubmissionsByAssignment(assignmentId);
            request.setAttribute("assignment", assignment);
            request.setAttribute("submissions", submissions);

            request.getRequestDispatcher("/WEB-INF/views/teacher/submissions.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
        }
    }
}
