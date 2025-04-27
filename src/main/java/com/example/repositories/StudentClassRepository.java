package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class StudentClassRepository {

    public void addStudentToClass(int studentID, int classID) throws SQLException {
        System.out.println("at addStudentToClass in StudentClassRepository");
        String query = "INSERT INTO studentclass (studentID, classID) VALUES (?, ?)";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }

    public void removeStudentFromClass(int studentID, int classID) throws SQLException {
        System.out.println("at removeStudentFromClass in StudentClassRepository");
        String query = "DELETE FROM studentclass WHERE studentID = ? AND classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, studentID);
        pstmt.setInt(2, classID);
        pstmt.executeUpdate();
    }
    
}
