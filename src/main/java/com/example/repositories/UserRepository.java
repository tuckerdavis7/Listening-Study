package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class UserRepository {
    public ResultSet getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
    
    public ResultSet getUserInfo(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
