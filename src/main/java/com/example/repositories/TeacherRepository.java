package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

public class TeacherRepository {
    public ResultSet getTeacherID(int userID) throws SQLException {
        String query = "SELECT ID FROM teachermaster WHERE user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
}
