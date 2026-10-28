-- ====================================================================
-- Database Schema for Student Assignment Management System (AIP Project)
-- Database: student_assignment_db
-- ====================================================================

DROP DATABASE IF EXISTS student_assignment_db;
CREATE DATABASE student_assignment_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_assignment_db;

-- --------------------------------------------------------------------
-- Table 1: users (Stores both Students and Teachers)
-- --------------------------------------------------------------------
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'TEACHER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- Table 2: assignments (Created and managed by Teachers)
-- --------------------------------------------------------------------
CREATE TABLE assignments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    due_date DATE NOT NULL,
    teacher_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_assignment_teacher FOREIGN KEY (teacher_id) 
        REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- Table 3: submissions (Student submissions, grading & feedback)
-- --------------------------------------------------------------------
CREATE TABLE submissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    assignment_id INT NOT NULL,
    student_id INT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    status ENUM('SUBMITTED', 'EVALUATED') DEFAULT 'SUBMITTED',
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    marks INT DEFAULT NULL,
    feedback TEXT DEFAULT NULL,
    evaluated_at TIMESTAMP NULL DEFAULT NULL,
    CONSTRAINT fk_submission_assignment FOREIGN KEY (assignment_id) 
        REFERENCES assignments(id) ON DELETE CASCADE,
    CONSTRAINT fk_submission_student FOREIGN KEY (student_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_student_assignment UNIQUE (assignment_id, student_id)
) ENGINE=InnoDB;

-- --------------------------------------------------------------------
-- Sample Seed Data
-- Passwords are encrypted using BCrypt:
-- Teacher login: teacher@college.edu / teacher123
-- Student login: student@college.edu / student123
-- --------------------------------------------------------------------
INSERT INTO users (full_name, email, password, role) VALUES 
('Prof. Sharma', 'teacher@college.edu', '$2a$10$UiCTFLQNlBh6NbPx1z3ebuSCtIor2i88314kflp1YzoS7FQV.aseC', 'TEACHER'),
('Rahul Verma', 'student@college.edu', '$2a$10$z1TUu/r0s1JBQblWDXR46O6ekcVQU4rXHwyeLyuCInU1OOxhtc2Sy', 'STUDENT');

