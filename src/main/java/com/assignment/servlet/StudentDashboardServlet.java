package com.assignment.servlet;

import com.assignment.dao.AssignmentDAO;
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
 * Servlet for displaying the Student Portal Dashboard.
 */
@WebServlet(name = "StudentDashboardServlet", urlPatterns = {"/student/dashboard"})
public class StudentDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AssignmentDAO assignmentDAO;

    @Override
    public void init() {
        this.assignmentDAO = new AssignmentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User student = (User) session.getAttribute("user");

        List<Assignment> assignments = assignmentDAO.getAllAssignmentsWithStudentStatus(student.getId());

        int totalAvailable = assignments.size();
        int submittedCount = 0;
        int evaluatedCount = 0;

        for (Assignment a : assignments) {
            if (a.isSubmitted()) {
                submittedCount++;
            }
            if (a.isEvaluated()) {
                evaluatedCount++;
            }
        }
        int pendingCount = totalAvailable - submittedCount;

        request.setAttribute("assignments", assignments);
        request.setAttribute("totalAvailable", totalAvailable);
        request.setAttribute("submittedCount", submittedCount);
        request.setAttribute("evaluatedCount", evaluatedCount);
        request.setAttribute("pendingCount", pendingCount);

        request.getRequestDispatcher("/WEB-INF/views/student/dashboard.jsp").forward(request, response);
    }
}
