package com.assignment.servlet;

import com.assignment.dao.UserDAO;
import com.assignment.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet handling new Student registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() {
        this.userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");

        // Set attributes to retain form values if validation fails
        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("role", role);

        // Validation 1: Required fields
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            confirmPassword == null || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validation 2: Email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            request.setAttribute("error", "Please provide a valid email address.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validation 3: Password length
        if (password.length() < 6) {
            request.setAttribute("error", "Password must be at least 6 characters long.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validation 4: Password confirmation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        // Validation 5: Role restriction (Defaults to STUDENT if not provided or valid)
        if (role == null || (!role.equalsIgnoreCase("STUDENT") && !role.equalsIgnoreCase("TEACHER"))) {
            role = "STUDENT";
        }

        // Validation 6: Duplicate Email Check
        if (userDAO.emailExists(email)) {
            request.setAttribute("error", "An account with this email already exists. Please log in.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        User newUser = new User(fullName.trim(), email.trim(), password, role);
        boolean success = userDAO.register(newUser);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?registered=true");
        } else {
            request.setAttribute("error", "Registration failed due to a database error. Please try again.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
