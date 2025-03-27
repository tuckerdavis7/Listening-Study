package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configuration.DatabaseConfiguration;

public class UserRepository {
    public void addUser(Map<String, Object> user) throws SQLException {
        String query = "INSERT INTO users (username, email, first_name, last_name, deleted, role, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, user.get("username").toString());
        pstmt.setString(2, user.get("email").toString());
        pstmt.setString(3, user.get("firstName").toString());
        pstmt.setString(4, user.get("lastName").toString());
        pstmt.setInt(5, 0);
        pstmt.setString(6, user.get("accountType").toString());
        pstmt.setString(7, user.get("password").toString());
        pstmt.executeUpdate();
    }

    public void deleteUser(Map<String, Object> user) throws SQLException {
        String query = "UPDATE users SET deleted = ? WHERE username = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1,1);
        pstmt.setString(2, user.get("username").toString());
        pstmt.executeUpdate();
    }

    public ResultSet getAllUsers() throws SQLException {
        String query = "SELECT * FROM users WHERE deleted =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, 0);
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

    public ResultSet getUserCountByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email =? AND deleted = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getUserCountByUsername(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username =? AND deleted = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public void updateDesignation(Map<String, Object> user) throws SQLException {
        String query = "UPDATE users SET role = ? WHERE username = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, user.get("role").toString());
        pstmt.setString(2, user.get("username").toString());
        pstmt.executeUpdate();
    }
}
