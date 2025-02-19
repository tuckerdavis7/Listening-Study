package com.example.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.DatabaseConnectionApp;

public class UserModel {
    public static ResultSet selectAllFirstAndLastNames() throws SQLException {
        String query = "SELECT first_name, last_name FROM users";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }
}
