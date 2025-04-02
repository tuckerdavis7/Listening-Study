package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configuration.DatabaseConfiguration;

public class QuizSettingsRepository {

    public ResultSet getQuizSettingsByID(Object classID) throws SQLException {
        String query = "SELECT * FROM playlist WHERE classID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) classID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

    public void addQuizSettings(int playlistID, String playbackMethod, int playbackDuration, int numQuestions) throws SQLException {
        String query = "INSERT INTO quizSettings (playlistID, playbackMethod, playbackDuration, numQuestions) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playlistID);
        pstmt.setString(2, playbackMethod);
        pstmt.setInt(3, playbackDuration);
        pstmt.setInt(4, numQuestions);
        pstmt.executeUpdate();
    }
}