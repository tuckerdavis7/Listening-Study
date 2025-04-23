package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.example.configurations.DatabaseConfiguration;

public class SessionRepository {
     /**
     * Returns the most recent meta data added
     *
     * @throws SQLException When the query does not run properly
     */
    public void createSession(String sessionID, int userID, String userRole, Timestamp expiresAt) throws SQLException {
        String query = "INSERT INTO sessions (session_id, user_id, role, expires_at) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, sessionID);
        pstmt.setInt(2, userID);
        pstmt.setString(3, userRole);
        pstmt.setTimestamp(4, expiresAt);    
        pstmt.executeUpdate();
    }

    public void deleteSession(String sessionID) throws SQLException {
        String query = "DELETE FROM sessions WHERE session_id = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, sessionID);
        pstmt.executeUpdate();
    }

    public Integer getSessionUserID(String sessionID) throws SQLException {
        String query = "SELECT user_id FROM sessions WHERE session_id = ? AND expires_at > CURRENT_TIMESTAMP";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, sessionID);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()) {
            return rs.getInt("user_id");
        }
        return null;
    }

    /**
     * Returns the session details including the user's role based on a valid session ID.
     *
     * @param sessionID The session ID of the user
     * @throws SQLException When the query fails to execute properly
     * @return ResultSet containing session information if the session is valid and not expired
     */
    public ResultSet getUserRoleBySessionID(String sessionID) throws SQLException {
        String query = "SELECT * FROM sessions where session_id = ? and expires_at > CURRENT_TIMESTAMP";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, sessionID);
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }
}
