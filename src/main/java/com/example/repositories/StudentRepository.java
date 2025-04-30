package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class StudentRepository {
    public ResultSet getStudentRoster(int classID) throws SQLException {
        String query = "SELECT studentID, studentEmail, studentFirstname, studentLastname FROM view_class WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getStudentByEmail(String studentEmail) throws SQLException {
        String query = "SELECT * FROM student WHERE Email = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, studentEmail);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getStudentByUserID(int userID) throws SQLException {
        String query = "SELECT * FROM student WHERE user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
    
}
