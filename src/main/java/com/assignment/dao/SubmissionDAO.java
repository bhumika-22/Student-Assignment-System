package com.assignment.dao;

import com.assignment.model.Submission;
import com.assignment.util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Assignment Submissions and Grading.
 */
public class SubmissionDAO {

    /**
     * Creates or updates a student submission (resubmission support).
     */
    public boolean saveOrUpdateSubmission(Submission sub) {
        String checkSql = "SELECT id FROM submissions WHERE assignment_id = ? AND student_id = ?";
        String insertSql = "INSERT INTO submissions (assignment_id, student_id, original_filename, stored_filename, file_path, file_size, status) VALUES (?, ?, ?, ?, ?, ?, 'SUBMITTED')";
        String updateSql = "UPDATE submissions SET original_filename = ?, stored_filename = ?, file_path = ?, file_size = ?, status = 'SUBMITTED', submitted_at = CURRENT_TIMESTAMP, marks = NULL, feedback = NULL, evaluated_at = NULL WHERE id = ?";

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement writeStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, sub.getAssignmentId());
            checkStmt.setInt(2, sub.getStudentId());
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                int existingId = rs.getInt("id");
                sub.setId(existingId);
                writeStmt = conn.prepareStatement(updateSql);
                writeStmt.setString(1, sub.getOriginalFilename());
                writeStmt.setString(2, sub.getStoredFilename());
                writeStmt.setString(3, sub.getFilePath());
                writeStmt.setLong(4, sub.getFileSize());
                writeStmt.setInt(5, existingId);
                return writeStmt.executeUpdate() > 0;
            } else {
                writeStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                writeStmt.setInt(1, sub.getAssignmentId());
                writeStmt.setInt(2, sub.getStudentId());
                writeStmt.setString(3, sub.getOriginalFilename());
                writeStmt.setString(4, sub.getStoredFilename());
                writeStmt.setString(5, sub.getFilePath());
                writeStmt.setLong(6, sub.getFileSize());

                int affected = writeStmt.executeUpdate();
                if (affected > 0) {
                    try (ResultSet keys = writeStmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            sub.setId(keys.getInt(1));
                        }
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving submission: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(checkStmt);
            DBConnectionUtil.closeQuietly(writeStmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return false;
    }

    /**
     * Retrieves a submission by ID with joined student and assignment data.
     */
    public Submission getSubmissionById(int id) {
        String sql = "SELECT s.*, a.title AS assignment_title, a.subject, a.due_date, " +
                     "u.full_name AS student_name, u.email AS student_email " +
                     "FROM submissions s " +
                     "JOIN assignments a ON s.assignment_id = a.id " +
                     "JOIN users u ON s.student_id = u.id " +
                     "WHERE s.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToSubmission(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching submission by ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return null;
    }

    /**
     * Retrieves all submissions for a specific assignment.
     */
    public List<Submission> getSubmissionsByAssignment(int assignmentId) {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT s.*, a.title AS assignment_title, a.subject, a.due_date, " +
                     "u.full_name AS student_name, u.email AS student_email " +
                     "FROM submissions s " +
                     "JOIN assignments a ON s.assignment_id = a.id " +
                     "JOIN users u ON s.student_id = u.id " +
                     "WHERE s.assignment_id = ? " +
                     "ORDER BY s.submitted_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, assignmentId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapResultSetToSubmission(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching submissions for assignment: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return list;
    }

    /**
     * Evaluates a submission by assigning marks and teacher feedback.
     */
    public boolean gradeSubmission(int submissionId, int marks, String feedback) {
        String sql = "UPDATE submissions SET marks = ?, feedback = ?, status = 'EVALUATED', evaluated_at = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DBConnectionUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, marks);
            stmt.setString(2, feedback != null ? feedback.trim() : "");
            stmt.setInt(3, submissionId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error grading submission: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return false;
    }

    /**
     * Total submissions count for all assignments created by a teacher.
     */
    public int getTotalSubmissionsForTeacher(int teacherId) {
        String sql = "SELECT COUNT(*) FROM submissions s JOIN assignments a ON s.assignment_id = a.id WHERE a.teacher_id = ?";
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
            System.err.println("Error getting total submissions for teacher: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return 0;
    }

    /**
     * Pending (unevaluated) submissions count for a teacher.
     */
    public int getPendingEvaluationsForTeacher(int teacherId) {
        String sql = "SELECT COUNT(*) FROM submissions s JOIN assignments a ON s.assignment_id = a.id WHERE a.teacher_id = ? AND s.status = 'SUBMITTED'";
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
            System.err.println("Error getting pending evaluations for teacher: " + e.getMessage());
        } finally {
            DBConnectionUtil.closeQuietly(rs);
            DBConnectionUtil.closeQuietly(stmt);
            DBConnectionUtil.closeQuietly(conn);
        }
        return 0;
    }

    private Submission mapResultSetToSubmission(ResultSet rs) throws SQLException {
        Submission s = new Submission();
        s.setId(rs.getInt("id"));
        s.setAssignmentId(rs.getInt("assignment_id"));
        s.setStudentId(rs.getInt("student_id"));
        s.setOriginalFilename(rs.getString("original_filename"));
        s.setStoredFilename(rs.getString("stored_filename"));
        s.setFilePath(rs.getString("file_path"));
        s.setFileSize(rs.getLong("file_size"));
        s.setStatus(rs.getString("status"));
        s.setSubmittedAt(rs.getTimestamp("submitted_at"));

        int marks = rs.getInt("marks");
        if (!rs.wasNull()) {
            s.setMarks(marks);
        }
        s.setFeedback(rs.getString("feedback"));
        s.setEvaluatedAt(rs.getTimestamp("evaluated_at"));

        // Helper fields
        s.setAssignmentTitle(rs.getString("assignment_title"));
        s.setSubject(rs.getString("subject"));
        s.setDueDate(rs.getDate("due_date"));
        s.setStudentName(rs.getString("student_name"));
        s.setStudentEmail(rs.getString("student_email"));
        return s;
    }
}
