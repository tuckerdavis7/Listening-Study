package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class QuizResultsRepository {

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

    public ResultSet getQuizResultsByID(Object quizSettingsID) throws SQLException {
        String query = "SELECT * FROM quizResults WHERE quizSettingsID = ? and deleted = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) quizSettingsID);
        pstmt.setInt(2, 0);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public void setDeletedByID(int quizSettingsID) throws SQLException {
        String query = "UPDATE quizResults set deleted = 1 where quizSettingsID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (Integer) quizSettingsID);
        pstmt.executeUpdate();
    }

}