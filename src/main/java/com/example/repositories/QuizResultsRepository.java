package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the quiz results table.
 */
public class QuizResultsRepository {

    /**
     * Adds active quiz's results to to the quiz results table
     *
     * @param quizSettingsID The ID of the active quiz
     * @param songName The name of the song
     * @param songComposer The composer of the song
     * @param songYear The year of the song
     * @param songID The ID of the song
     * @throws SQLException When the query does not run properly
     */
    public void addQuizResults(int quizSettingsID, String songName, String songComposer, String songYear, int songID, int userID, int numQuestions) throws SQLException {
        String query = "INSERT INTO quizResults (quizSettingsID, songName, songComposer, songYear, songID, userID, numQuestions, deleted) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, quizSettingsID);
        pstmt.setString(2, songName);
        pstmt.setString(3, songComposer);
        pstmt.setString(4, songYear);
        pstmt.setInt(5, songID);
        pstmt.setInt(6, userID);
        pstmt.setInt(7, numQuestions);
        pstmt.setInt(8, 0);
        pstmt.executeUpdate();
    }
   
     /**
     * Gets Quiz settings by the userID
     *
     * @param userID The ID of the active quiz
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getQuizResults(int userID) throws SQLException {
        String query = "SELECT * FROM quizResults WHERE deleted = 0 AND userID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Sets deleted field in quiz results table to 1 by userID
     *
     * @param userID The ID of the active quiz
     * @throws SQLException When the query does not run properly
     */
    public void setDeletedByID(int userID) throws SQLException {
        String query = "UPDATE quizResults SET deleted = 1 WHERE deleted = 0 AND userID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        pstmt.executeUpdate();
    }

}