package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class StudentClassRepository {

    public void addStudentToClass(int studentID, int classID) throws SQLException {
        String query = "INSERT INTO studentclass (studentID, classID) VALUES (?, ?)";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }

    public void removeStudentFromClass(int studentID, int classID) throws SQLException {
        String query = "DELETE FROM studentclass WHERE studentID = ? AND classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }

    public void removeAllStudentsFromClass(int classID) throws SQLException {
        String query = "DELETE FROM studentclass WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        pstmt.executeUpdate();
    }
    
    public ResultSet getClassIDByStudentID(int studentID) throws SQLException {
        String query = "SELECT * FROM studentclass WHERE studentID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, studentID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
}
