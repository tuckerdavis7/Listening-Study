package com.example.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.DatabaseConnectionApp;

public class LoginModel {
    public static ResultSet verifyUser(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email =?";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
