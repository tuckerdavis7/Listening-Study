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
     * Returns the playlist by Class ID.
     *
     * @param classIDs The list of active class IDs to search for
     * @return ResultSet containing query results of playlists associated with the provided class IDs
     * @throws SQLException When the database query does not execute properly
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

    /**
     * Retrieves playlists associated with a specific teacher.
     *
     * @param teacherID The ID of the teacher
     * @return ResultSet containing playlists created by the specified teacher
     * @throws SQLException When the database query does not execute properly
     */
    public ResultSet getPlaylistByTeacherID(Object teacherID) throws SQLException {
        String query = "select p.ID, p.playlistName, c.className, c.teacherID from playlist p "
                         + "left join class c on p.classID = c.ID" 
                         + " WHERE p.teacherID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) teacherID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Retrieves playlists accessible to a specific student based on their classes.
     *
     * @param studentID The ID of the student
     * @return ResultSet containing playlists available to the specified student
     * @throws SQLException When the database query does not execute properly
     */
    public ResultSet getPlaylistByStudentID(Object studentID) throws SQLException {
        String query = "select p.ID, p.playlistName, p.teacherID, p.classID, c.className from playlist p "
                         + "left join studentClass sc on sc.classID = p.classID" 
                         + " left join class c on p.ClassID = c.id"
                         + " where sc.studentId = ?";                        

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) studentID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Creates a new playlist in the database.
     *
     * @param playlistName The name of the playlist to create
     * @param teacherID The ID of the teacher creating the playlist
     * @param classID The ID of the class this playlist belongs to
     * @throws SQLException When the database insertion does not execute properly
     */
    public void createPlaylist(String playlistName, int teacherID, int classID) throws SQLException {
        String query = "INSERT INTO playlist (playlistName, teacherID, classID) VALUES (?, ?, ?)";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, playlistName);
        pstmt.setInt(2, teacherID);
        pstmt.setInt(3, classID);
        pstmt.executeUpdate();
    }

    /**
     * Updates the name of an existing playlist.
     *
     * @param playlistID The ID of the playlist to rename
     * @param newPlaylistName The new name for the playlist
     * @throws SQLException When the database update does not execute properly
     */
    public void renamePlaylist(Object playlistID, Object newPlaylistName) throws SQLException {
        String query = "UPDATE playlist SET playlistName = ? WHERE ID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, (String) newPlaylistName);
        pstmt.setInt(2, (Integer) playlistID);
        pstmt.executeUpdate();
    }

    /**
     * Retrieves the playlist ID for a specific teacher and class combination.
     *
     * @param teacherID The ID of the teacher
     * @param classID The ID of the class
     * @return ResultSet containing the ID of matching playlist(s)
     * @throws SQLException When the database query does not execute properly
     */
    public ResultSet getPlaylistID(int teacherID, int classID) throws SQLException {
        String query = "SELECT ID FROM playlist WHERE teacherID = ? and classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, teacherID);
        pstmt.setInt(2, classID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Retrieves all playlist IDs accessible to a specific user, whether as a student or teacher.
     * Uses a UNION query to combine results from both roles.
     *
     * @param userID The ID of the user
     * @return ResultSet containing all playlist IDs the user has access to
     * @throws SQLException When the database query does not execute properly
     */
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

    /**
     * Retrieves the name of a playlist by its ID.
     *
     * @param playlistID The ID of the playlist
     * @return ResultSet containing the name of the specified playlist
     * @throws SQLException When the database query does not execute properly
     */
    public ResultSet getPlaylistNameByID(int playlistID) throws SQLException {
        String query = "SELECT playlistName FROM playlist WHERE ID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playlistID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Deletes all playlists associated with a specific class.
     *
     * @param classID The ID of the class whose playlists should be deleted
     * @throws SQLException When the database deletion does not execute properly
     */
    public void deletePlaylistByClassID(int classID) throws SQLException {
        String query = "DELETE FROM playlist WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        pstmt.executeUpdate();
    }
    
}