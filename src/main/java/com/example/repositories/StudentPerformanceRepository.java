package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class StudentPerformanceRepository {
     public ResultSet getPerformanceByUsername(Object studentID) throws SQLException {
        String query = "SELECT sp.*, s.songName, s.songComposer, s.songYear, s.youtubeLink, c.className, p.playlistName " +
        "FROM studentPerformance sp " +
        "JOIN song s ON sp.SongID = s.ID " +
        "JOIN class c ON sp.ClassID = c.ID " +
        "JOIN playlist p ON sp.PlaylistID = p.ID " +
        "WHERE sp.StudentID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) studentID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
