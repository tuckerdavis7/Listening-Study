package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class ClassRepository {
      public ResultSet getClasslist(int teacherID) throws SQLException {
        String query = "SELECT * FROM class_overview WHERE teacher_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, teacherID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
    
}
