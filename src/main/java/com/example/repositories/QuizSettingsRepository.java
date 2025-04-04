package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class QuizSettingsRepository {

    public void addQuizSettings(int playlistID, String playbackMethod, int playbackDuration, int numQuestions) throws SQLException {
        String query = "INSERT INTO quizSettings (playlistID, playbackMethod, playbackDuration, numQuestions) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playlistID);
        pstmt.setString(2, playbackMethod);
        pstmt.setInt(3, playbackDuration);
        pstmt.setInt(4, numQuestions);
        pstmt.executeUpdate();
    }

    public ResultSet getQuizSettingsByID(Object playlistID) throws SQLException {
        String query = "SELECT * FROM quizSettings WHERE playlistID = ? and deleted = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) playlistID);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public ResultSet getNumQuestionsByID(Object quizSettingsID) throws SQLException {
        String query = "SELECT * FROM quizSettings WHERE ID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) quizSettingsID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public void setDeletedByID(int quizSettingsID) throws SQLException {
        String query = "UPDATE quizSettings set deleted = 1 where ID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) quizSettingsID);
        pstmt.executeUpdate();
    }

}