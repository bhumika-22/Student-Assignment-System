package com.assignment.servlet;

import com.assignment.dao.UserDAO;
import com.assignment.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet handling user authentication and login session creation.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String role = (String) session.getAttribute("role");
            if ("TEACHER".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
                return;
            } else if ("STUDENT".equalsIgnoreCase(role)) {
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
                return;
            }
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Input validation
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and Password are required fields.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        User user = userDAO.authenticate(email, password);

        if (user != null) {
            // Invalidate old session to prevent session fixation attack
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            // Create fresh session
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", user);
            newSession.setAttribute("userId", user.getId());
            newSession.setAttribute("userName", user.getFullName());
            newSession.setAttribute("userEmail", user.getEmail());
            newSession.setAttribute("role", user.getRole());

            // Role-based redirection
            if (user.isTeacher()) {
                response.sendRedirect(request.getContextPath() + "/teacher/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
            }
        } else {
            request.setAttribute("error", "Invalid email address or password. Please try again.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
