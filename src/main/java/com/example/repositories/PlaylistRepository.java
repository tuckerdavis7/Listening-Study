package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public ResultSet getPlaylistByClassID(Object classID) throws SQLException {
        String query = "SELECT * FROM playlist WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) classID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

    public ResultSet getPlaylistByTeacherID(Object teacherID) throws SQLException {
        String query = "select p.ID, p.playlistName, c.className, c.teacherID from playlist p "
                         + "left join class c on p.classID = c.ID" 
                         + "WHERE teacherID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) teacherID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
    
}
