package com.assignment.filter;

import com.assignment.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Authentication and Role-based Authorization Filter.
 * Protects /teacher/* and /student/* routes against unauthorized access.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/teacher/*", "/student/*", "/download"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Prevent browser caching of secured dashboard pages
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        HttpSession session = httpRequest.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Not authenticated: redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?sessionExpired=true");
            return;
        }

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String relativePath = uri.substring(contextPath.length());

        // Role-based route authorization
        if (relativePath.startsWith("/teacher/") && !user.isTeacher()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/student/dashboard");
            return;
        }

        if (relativePath.startsWith("/student/") && !user.isStudent()) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/teacher/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
