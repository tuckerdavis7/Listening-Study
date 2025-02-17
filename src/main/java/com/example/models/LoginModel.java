package com.example.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.DatabaseConnectionApp;

public class LoginModel {
    public static boolean verifyUser(String email, String password) throws SQLException {
        String query = "SELECT email, password FROM users WHERE email =?";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

        //true if user exists, otherwise false
       return rs.next();
    }
}
