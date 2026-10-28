package com.assignment.dao;

import com.assignment.model.Assignment;
import com.assignment.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Assignment CRUD operations.
 */
public class AssignmentDAO {

    /**
     * Creates a new assignment posted by a teacher.
     */
    public boolean createAssignment(Assignment assignment) {
        String sql = "INSERT INTO assignments (title, subject, description, due_date, teacher_id) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, assignment.getTitle().trim());
            stmt.setString(2, assignment.getSubject().trim());
            stmt.setString(3, assignment.getDescription().trim());
            stmt.setDate(4, assignment.getDueDate());
            stmt.setInt(5, assignment.getTeacherId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        assignment.setId(keys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating assignment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return false;
    }

    /**
     * Retrieves an assignment by ID along with teacher name and submission count.
     */
    public Assignment getAssignmentById(int id) {
        String sql = "SELECT a.*, u.full_name AS teacher_name, " +
                     "(SELECT COUNT(*) FROM submissions s WHERE s.assignment_id = a.id) AS submission_count " +
                     "FROM assignments a " +
                     "JOIN users u ON a.teacher_id = u.id " +
                     "WHERE a.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Assignment a = new Assignment();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setSubject(rs.getString("subject"));
                a.setDescription(rs.getString("description"));
                a.setDueDate(rs.getDate("due_date"));
                a.setTeacherId(rs.getInt("teacher_id"));
                a.setTeacherName(rs.getString("teacher_name"));
                a.setSubmissionCount(rs.getInt("submission_count"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));
                return a;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching assignment by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return null;
    }

    /**
     * Retrieves all assignments created by a specific teacher, ordered by newest first.
     */
    public List<Assignment> getAssignmentsByTeacher(int teacherId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, " +
                     "(SELECT COUNT(*) FROM submissions s WHERE s.assignment_id = a.id) AS submission_count " +
                     "FROM assignments a " +
                     "WHERE a.teacher_id = ? " +
                     "ORDER BY a.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teacherId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Assignment a = new Assignment();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setSubject(rs.getString("subject"));
                a.setDescription(rs.getString("description"));
                a.setDueDate(rs.getDate("due_date"));
                a.setTeacherId(rs.getInt("teacher_id"));
                a.setSubmissionCount(rs.getInt("submission_count"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching teacher assignments: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return list;
    }

    /**
     * Retrieves all assignments with the current student's submission status.
     */
    public List<Assignment> getAllAssignmentsWithStudentStatus(int studentId) {
        List<Assignment> list = new ArrayList<>();
        String sql = "SELECT a.*, u.full_name AS teacher_name, " +
                     "s.id AS submission_id, s.status AS submission_status, " +
                     "s.marks, s.feedback, s.submitted_at, s.original_filename, s.stored_filename " +
                     "FROM assignments a " +
                     "JOIN users u ON a.teacher_id = u.id " +
                     "LEFT JOIN submissions s ON a.id = s.assignment_id AND s.student_id = ? " +
                     "ORDER BY a.due_date ASC, a.created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Assignment a = new Assignment();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setSubject(rs.getString("subject"));
                a.setDescription(rs.getString("description"));
                a.setDueDate(rs.getDate("due_date"));
                a.setTeacherId(rs.getInt("teacher_id"));
                a.setTeacherName(rs.getString("teacher_name"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                a.setUpdatedAt(rs.getTimestamp("updated_at"));

                // Student submission info
                int subId = rs.getInt("submission_id");
                if (!rs.wasNull()) {
                    a.setSubmissionId(subId);
                    a.setSubmissionStatus(rs.getString("submission_status"));
                    int marks = rs.getInt("marks");
                    if (!rs.wasNull()) {
                        a.setMarks(marks);
                    }
                    a.setFeedback(rs.getString("feedback"));
                    a.setSubmittedAt(rs.getTimestamp("submitted_at"));
                    a.setOriginalFilename(rs.getString("original_filename"));
                    a.setStoredFilename(rs.getString("stored_filename"));
                }

                list.add(a);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching assignments with student status: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return list;
    }

    /**
     * Deletes an assignment owned by a specific teacher.
     */
    public boolean deleteAssignment(int assignmentId, int teacherId) {
        String sql = "DELETE FROM assignments WHERE id = ? AND teacher_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignmentId);
            stmt.setInt(2, teacherId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting assignment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return false;
    }

    /**
     * Gets the count of assignments created by a teacher.
     */
    public int getTeacherAssignmentsCount(int teacherId) {
        String sql = "SELECT COUNT(*) FROM assignments WHERE teacher_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teacherId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting teacher assignment count: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return 0;
    }
}
