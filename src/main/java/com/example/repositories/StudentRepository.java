package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class StudentRepository {
    public ResultSet getStudentRoster(int classID) throws SQLException {
        String query = "SELECT ID, Email, Firstname, LastName FROM student WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
    
}
