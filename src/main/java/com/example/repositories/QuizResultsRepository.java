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
    public void addQuizResults(int quizSettingsID, String songName, String songComposer, String songYear, int songID) throws SQLException {
        String query = "INSERT INTO quizResults (quizSettingsID, songName, songComposer, songYear, songID, deleted) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, quizSettingsID);
        pstmt.setString(2, songName);
        pstmt.setString(3, songComposer);
        pstmt.setString(4, songYear);
        pstmt.setInt(5, songID);
        pstmt.setInt(6, 0);
        pstmt.executeUpdate();
    }

    /**
     * Returns the quiz results by quizSettingsID from the quiz results table
     *
     * @param quizSettingsID The ID of the active quiz
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    // public ResultSet getQuizResultsByID(Object quizSettingsID) throws SQLException {
    //     String query = "SELECT * FROM quizResults WHERE quizSettingsID = ? and deleted = ?";

    //     PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    //     pstmt.setInt(1, (Integer) quizSettingsID);
    //     pstmt.setInt(2, 0);
    //     ResultSet rs = pstmt.executeQuery();

    //     return rs;
    // }

    public ResultSet getQuizResults() throws SQLException {
        String query = "SELECT * FROM quizResults WHERE deleted = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Sets deleted field in quiz results table to 1 by quizSettingsID
     *
     * @param quizSettingsID The ID of the active quiz
     * @throws SQLException When the query does not run properly
     */
    public void setDeletedByID(int quizSettingsID) throws SQLException {
        String query = "UPDATE quizResults set deleted = 1 where quizSettingsID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) quizSettingsID);
        pstmt.executeUpdate();
    }

}