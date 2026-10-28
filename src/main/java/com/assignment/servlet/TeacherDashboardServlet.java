package com.assignment.servlet;

import com.assignment.dao.AssignmentDAO;
import com.assignment.dao.SubmissionDAO;
import com.assignment.model.Assignment;
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
 * Servlet for displaying the Teacher Portal Dashboard.
 */
@WebServlet(name = "TeacherDashboardServlet", urlPatterns = {"/teacher/dashboard"})
public class TeacherDashboardServlet extends HttpServlet {
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

        List<Assignment> assignments = assignmentDAO.getAssignmentsByTeacher(teacher.getId());
        int totalAssignments = assignments.size();
        int totalSubmissions = submissionDAO.getTotalSubmissionsForTeacher(teacher.getId());
        int pendingEvaluations = submissionDAO.getPendingEvaluationsForTeacher(teacher.getId());

        request.setAttribute("assignments", assignments);
        request.setAttribute("totalAssignments", totalAssignments);
        request.setAttribute("totalSubmissions", totalSubmissions);
        request.setAttribute("pendingEvaluations", pendingEvaluations);

        request.getRequestDispatcher("/WEB-INF/views/teacher/dashboard.jsp").forward(request, response);
    }
}
