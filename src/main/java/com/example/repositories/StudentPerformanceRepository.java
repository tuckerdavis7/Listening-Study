package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

/**
 * Repository class to execute queries on the student performance table.
 */
public class StudentPerformanceRepository {

    /**
     * gets the student performance by ID
     *
     * @param StudentID The ID of the student
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
   public ResultSet getPerformanceByID(Object studentID) throws SQLException {
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

    /**
     * returns the weight of the question by song and studentID
     *
     * @param StudentID The ID of the student
     * @param SongID The ID of the song
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
   public ResultSet getSongWeight(int songID, int studentID) throws SQLException {
      String query = "SELECT Weight FROM studentPerformance WHERE songID = ? AND studentID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, (Integer) songID);
      pstmt.setInt(2, (Integer) studentID);
      ResultSet rs = pstmt.executeQuery();

      return rs;
   }

    /**
     * returns the performance data if a song on a playlist
     *
     * @param StudentID The ID of the student
     * @param PlaylistID The ID of the student
     * @param SongID The ID of the song
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getPerformanceData(int studentID, int playlistID, int songID) throws SQLException {
      String query = "SELECT *" +
      "FROM studentPerformance" +      
      "WHERE StudentID = ? and PlayListID = ? and SongID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, (Integer) studentID);
      pstmt.setInt(1, (Integer) playlistID);
      pstmt.setInt(1, (Integer) songID);
      ResultSet rs = pstmt.executeQuery();

     return rs;
  }

  /**
     * updates the performance data of a song
     *
     * @param timesQuizzed number of times a question was quizzed
     * @param timescorrect number of times a student got a question correct
     * @param weight the weight of a song
     * @param rate the rate of a song
     * @param ID The ID of performance
     * @throws SQLException When the query does not run properly
     */
  public void updatePerformanceData(int timesQuizzed, int timesCorrect, double weight, double rate, int ID) throws SQLException {
   String query = "UPDATE studentPerformance " +
                   "SET weight = ?, score = ?, timesQuizzed = ?, timesCorrect = ? " +
                   "WHERE ID = ?";

    try (PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query)) {
        pstmt.setDouble(1, weight);
        pstmt.setDouble(2, rate); // Assuming "score" = "rate"
        pstmt.setInt(3, timesQuizzed);
        pstmt.setInt(4, timesCorrect);
        pstmt.setInt(5, ID);

        pstmt.executeUpdate();
    }

}
}
