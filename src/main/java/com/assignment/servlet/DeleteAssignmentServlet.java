package com.assignment.servlet;

import com.assignment.dao.AssignmentDAO;
import com.assignment.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet handling assignment deletion by its creator teacher.
 */
@WebServlet(name = "DeleteAssignmentServlet", urlPatterns = {"/teacher/delete-assignment"})
public class DeleteAssignmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private AssignmentDAO assignmentDAO;

    @Override
    public void init() {
        this.assignmentDAO = new AssignmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User teacher = (User) session.getAttribute("user");

        String assignmentIdStr = request.getParameter("assignmentId");
        if (assignmentIdStr != null && !assignmentIdStr.trim().isEmpty()) {
            try {
                int assignmentId = Integer.parseInt(assignmentIdStr.trim());
                assignmentDAO.deleteAssignment(assignmentId, teacher.getId());
            } catch (NumberFormatException ignored) {
            }
        }
        response.sendRedirect(request.getContextPath() + "/teacher/dashboard?deleted=true");
    }
}
