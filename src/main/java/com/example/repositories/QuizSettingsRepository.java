package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

/**
 * Repository class to execute queries on the quiz settings table.
 */
public class QuizSettingsRepository {

    /**
     * Adds the most recent quiz settings to the table
     *
     * @param playlistID The ID of the active playlist
     * @param playbackMethod The current selected playback method for the quiz
     * @param playbackDuration The chosen duration of playback (as a number)
     * @param numQuestions The chosen number of questions for the current quiz settings
     * @throws SQLException When the query does not run properly
     */
    public void addQuizSettings(int playlistID, String playbackMethod, int playbackDuration, int numQuestions) throws SQLException {
        String query = "INSERT INTO quizSettings (playlistID, playbackMethod, playbackDuration, numQuestions) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playlistID);
        pstmt.setString(2, playbackMethod);
        pstmt.setInt(3, playbackDuration);
        pstmt.setInt(4, numQuestions);
        pstmt.executeUpdate();
    }

    /**
     * Returns the most recent quiz settings from the chosen playlistID
     *
     * @param playlistID The ID of the active playlist
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
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