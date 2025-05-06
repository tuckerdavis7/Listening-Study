package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the studentClass table.
 */
public class StudentClassRepository {

    /**
     * Adds a student to a specific class.
     *
     * @param studentID The ID of the student to be added
     * @param classID The ID of the class to which the student is added
     * @throws SQLException When the insert query fails
     */
    public void addStudentToClass(int studentID, int classID) throws SQLException {
        String query = "INSERT INTO studentclass (studentID, classID) VALUES (?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }

    /**
     * Removes a student from a specific class.
     *
     * @param studentID The ID of the student to be removed
     * @param classID The ID of the class from which the student is removed
     * @throws SQLException When the delete query fails
     */
    public void removeStudentFromClass(int studentID, int classID) throws SQLException {
        String query = "DELETE FROM studentclass WHERE studentID = ? AND classID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }

    /**
     * Removes all students from a specific class.
     *
     * @param classID The ID of the class whose student associations are to be removed
     * @throws SQLException When the delete query fails
     */
    public void removeAllStudentsFromClass(int classID) throws SQLException {
        String query = "DELETE FROM studentclass WHERE classID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, classID);
        pstmt.executeUpdate();
    }

    /**
     * Retrieves all class IDs associated with a given student.
     *
     * @param studentID The ID of the student
     * @return ResultSet containing all class associations for the student
     * @throws SQLException When the select query fails
     */
    public ResultSet getClassIDByStudentID(int studentID) throws SQLException {
        String query = "SELECT * FROM studentclass WHERE studentID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, studentID);
        return pstmt.executeQuery();
    }
}
