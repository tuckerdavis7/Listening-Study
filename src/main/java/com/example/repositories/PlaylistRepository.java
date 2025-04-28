package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the playlist table.
 */
public class PlaylistRepository {
    /**
     * Returns the playlist by Class ID
     *
     * @param ClassID The ID of the active classID
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getPlaylistByClassID(ArrayList<Integer> classIDs) throws SQLException {
        String placeholders = String.join(", ", java.util.Collections.nCopies(classIDs.size(), "?"));
        String query = "SELECT * FROM playlist WHERE classID IN ("+ placeholders + ")";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        for (int i = 0; i < classIDs.size(); i++) {
            pstmt.setInt(i + 1, classIDs.get(i));
        }
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getPlaylistByTeacherID(Object teacherID) throws SQLException {
        String query = "select p.ID, p.playlistName, c.className, c.teacherID from playlist p "
                         + "left join class c on p.classID = c.ID" 
                         + " WHERE p.teacherID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) teacherID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public void createPlaylist(String playlistName, int teacherID, int classID) throws SQLException {
        String query = "INSERT INTO playlist (playlistName, teacherID, classID) VALUES (?, ?, ?)";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, playlistName);
        pstmt.setInt(2, teacherID);
        pstmt.setInt(3, classID);
        pstmt.executeUpdate();
    }

    public void renamePlaylist(Object playlistID, Object newPlaylistName) throws SQLException {
        String query = "UPDATE playlist SET playlistName = ? WHERE ID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, (String) newPlaylistName);
        pstmt.setInt(2, (Integer) playlistID);
        pstmt.executeUpdate();
    }

    public ResultSet getPlaylistID(int teacherID, int classID) throws SQLException {
        String query = "SELECT ID FROM playlist WHERE teacherID = ? and classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, teacherID);
        pstmt.setInt(2, classID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getPlaylistIDsForUser(int userID) throws SQLException {
        String query = "(" + 
                        "    SELECT DISTINCT p.ID AS playlist_id" +
                        "    FROM users u" +
                        "    JOIN student s ON u.user_id = s.user_id" +
                        "    LEFT JOIN studentClass sc ON s.ID = sc.studentID" +
                        "    LEFT JOIN class c ON sc.classID = c.ID" +
                        "    JOIN playlist p ON p.classID = c.ID" +
                        "    WHERE u.user_id = ?" +
                        ")" +
                        "UNION" +
                        "(" +
                        "    SELECT p.ID AS playlist_id" +
                        "    FROM users u" +
                        "    JOIN teacherMaster t ON u.user_id = t.user_id" +
                        "    JOIN playlist p ON p.teacherID = t.ID" +
                        "    WHERE u.user_id = ?" +
                        ")";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        pstmt.setInt(2, userID);
        ResultSet rs = pstmt.executeQuery();
        return rs;
    }
    
}
