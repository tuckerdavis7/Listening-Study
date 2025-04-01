package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class StudentPerformanceRepository {
     public ResultSet getPerformanceByUsername(Object studentID) throws SQLException {
        String query = "SELECT * FROM studentPerformance WHERE StudentID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, (Integer) studentID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
