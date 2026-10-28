package com.assignment.dao;

import com.assignment.model.User;
import com.assignment.util.DBConnectionUtil;
import com.assignment.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for User database operations using PreparedStatement.
 */
public class UserDAO {

    /**
     * Finds a user by email address.
     *
     * @param email The user's email
     * @return User object if found, null otherwise
     */
    public User findByEmail(String email) {
        String sql = "SELECT id, full_name, email, password, role, created_at FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email.trim().toLowerCase());
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by email: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return null;
    }

    /**
     * Finds a user by primary key ID.
     *
     * @param id The user ID
     * @return User object if found, null otherwise
     */
    public User findById(int id) {
        String sql = "SELECT id, full_name, email, password, role, created_at FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by ID: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return null;
    }

    /**
     * Checks if an email is already registered.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email.trim().toLowerCase());
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking email existence: " + e.getMessage());
            return false;
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
    }

    /**
     * Registers a new user with BCrypt hashed password.
     *
     * @param user The user object with raw password
     * @return true if registration succeeds, false otherwise
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.getFullName().trim());
            stmt.setString(2, user.getEmail().trim().toLowerCase());
            stmt.setString(3, PasswordUtil.hashPassword(user.getPassword()));
            stmt.setString(4, user.getRole().toUpperCase());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return false;
    }

    /**
     * Authenticates a user by email and plain-text password using BCrypt.
     *
     * @param email         User email
     * @param plainPassword Raw password entered
     * @return User object on successful authentication, null otherwise
     */
    public User authenticate(String email, String plainPassword) {
        User user = findByEmail(email);
        if (user != null && PasswordUtil.checkPassword(plainPassword, user.getPassword())) {
            return user;
        }
        return null;
    }
}
