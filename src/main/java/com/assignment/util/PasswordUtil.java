package com.assignment.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords securely using BCrypt.
 */
public class PasswordUtil {

    /**
     * Hashes a plain-text password using BCrypt with a default work factor of 10.
     *
     * @param plainTextPassword The raw password entered by the user
     * @return The 60-character BCrypt hashed string
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(10));
    }

    /**
     * Checks if a plain-text password matches an existing BCrypt hashed password.
     *
     * @param plainTextPassword The raw password to test
     * @param hashedPassword    The stored hash from the database
     * @return true if matches, false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    // Quick test runner to print sample hashes
    public static void main(String[] args) {
        System.out.println("teacher123 -> " + hashPassword("teacher123"));
        System.out.println("student123 -> " + hashPassword("student123"));
    }
}
