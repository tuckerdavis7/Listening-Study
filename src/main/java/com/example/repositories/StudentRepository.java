package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class for retrieving student-related data from the database.
 */
public class StudentRepository {

    /**
     * Retrieves the roster of students in a specific class.
     *
     * @param classID The ID of the class
     * @return ResultSet containing student ID, email, first name, and last name
     * @throws SQLException When the query fails to execute
     */
    public ResultSet getStudentRoster(int classID) throws SQLException {
        String query = "SELECT studentID, studentEmail, studentFirstname, studentLastname FROM view_class WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        return pstmt.executeQuery();
    }

    /**
     * Retrieves student details by their email address.
     *
     * @param studentEmail The email of the student
     * @return ResultSet containing student record details
     * @throws SQLException When the query fails to execute
     */
    public ResultSet getStudentByEmail(String studentEmail) throws SQLException {
        String query = "SELECT * FROM student WHERE Email = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, studentEmail);
        return pstmt.executeQuery();
    }

    /**
     * Retrieves student details using the associated user ID.
     *
     * @param userID The user ID linked to the student
     * @return ResultSet containing student record details
     * @throws SQLException When the query fails to execute
     */
    public ResultSet getStudentByUserID(int userID) throws SQLException {
        String query = "SELECT * FROM student WHERE user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        return pstmt.executeQuery();
    }
}
