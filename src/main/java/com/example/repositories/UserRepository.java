package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.DatabaseConnectionApp;

public class UserRepository {
    public static ResultSet getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
    
    public static ResultSet getUserInfo(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id =?";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
}
