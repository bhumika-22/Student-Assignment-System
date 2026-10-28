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
import java.sql.Date;

/**
 * Servlet handling assignment creation by a teacher.
 */
@WebServlet(name = "CreateAssignmentServlet", urlPatterns = {"/teacher/create-assignment"})
public class CreateAssignmentServlet extends HttpServlet {
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

        String title = request.getParameter("title");
        String subject = request.getParameter("subject");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("dueDate");

        if (title == null || title.trim().isEmpty() ||
            subject == null || subject.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            dueDateStr == null || dueDateStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard?error=missing_fields");
            return;
        }

        try {
            Date dueDate = Date.valueOf(dueDateStr.trim());
            Assignment assignment = new Assignment();
            assignment.setTitle(title.trim());
            assignment.setSubject(subject.trim());
            assignment.setDescription(description.trim());
            assignment.setDueDate(dueDate);
            assignment.setTeacherId(teacher.getId());

            boolean success = assignmentDAO.createAssignment(assignment);
            if (success) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard?created=true");
            } else {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard?error=db_error");
            }
        } catch (IllegalArgumentException e) {
            response.sendRedirect(request.getContextPath() + "/teacher/dashboard?error=invalid_date");
        }
    }
}
