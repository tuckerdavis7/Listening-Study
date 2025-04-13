package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configuration.DatabaseConfiguration;

/**
 * Repository class to execute queries on the user table.
 */
public class UserRepository {
    
    /**
     * Adds the user to the user table
     *
     * @param Map user that contains the user and its data
     * @throws SQLException When the query does not run properly
     */
    public void addUser(Map<String, Object> user) throws SQLException {
        String query = "INSERT INTO users (email, first_name, last_name, deleted, role, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(2, user.get("email").toString());
        pstmt.setString(3, user.get("firstName").toString());
        pstmt.setString(4, user.get("lastName").toString());
        pstmt.setInt(5, 0);
        pstmt.setString(6, user.get("accountType").toString());
        pstmt.setString(7, user.get("password").toString());
        pstmt.executeUpdate();
    }

    /**
     * deletes user from the user table
     *
     * @param Map user that contains the user and its data
     * @throws SQLException When the query does not run properly
     */
    public void deleteUser(Map<String, Object> user) throws SQLException {
        String query = "UPDATE users SET deleted = ? WHERE email = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1,1);
        pstmt.setString(2, user.get("email").toString());
        pstmt.executeUpdate();
    } 

    /**
     * returns user by email
     *
     * @param email of the user
     * @throws SQLException When the query does not run properly
     * @return result set of the query
     */
    public ResultSet getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

    /**
     * returns count of a specific email
     *
     * @param email of the user
     * @throws SQLException When the query does not run properly
     * @return result set of the query
     */
    public ResultSet getUserCountByEmail(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE email =? AND deleted = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, email);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * updates a moderator or teacher role
     *
     * @param map of the user data
     * @throws SQLException When the query does not run properly
     */
    public void updateModeratorOrTeacherDesignation(Map<String, Object> user) throws SQLException {
        String query = "UPDATE users SET role = ? WHERE email = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, user.get("role").toString());
        pstmt.setString(2, user.get("email").toString());
        pstmt.executeUpdate();
    }
}
