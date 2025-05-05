package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

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
    public void addQuizSettings(int user_ID, String playbackMethod, int playbackDuration, int numQuestions, int playlistID) throws SQLException {
        String query = "INSERT INTO quizSettings (user_ID, playbackMethod, playbackDuration, numQuestions, playlistID) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, user_ID);
        pstmt.setString(2, playbackMethod);
        pstmt.setInt(3, playbackDuration);
        pstmt.setInt(4, numQuestions);
        pstmt.setInt(5, playlistID);
        pstmt.executeUpdate();
    }

    /**
     * Returns the most recent quiz settings from the chosen playlistID
     *
     * @param userID The ID of the current user.
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getQuizSettingsByID(int userID) throws SQLException {
        String query = "SELECT * FROM quizSettings WHERE user_id = ? and deleted = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }   

    /**
     * gets quiz settings by the user Id.
     *
     * @param userID The ID of the active user
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getQuizSettings(int userID) throws SQLException {
            String query = "SELECT * FROM quizSettings WHERE deleted = 0 AND user_id = ?";
    
            PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
    
            return rs;
        }

    /**
     * Sets deleted field in quiz settings table to 1 by quizSettingsID
     *
     * @param userID The ID of the active user
     * @throws SQLException When the query does not run properly
     */
    public void setDeletedByID(int userID) throws SQLException {
        String query = "UPDATE quizSettings SET deleted = 1 WHERE deleted = 0 AND user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        pstmt.executeUpdate();
    }

}