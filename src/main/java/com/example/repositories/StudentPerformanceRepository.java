package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

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
      String query = "SELECT sp.*, s.songName, s.songComposer, s.songYear, s.youtubeLink, p.playlistName " +
      "FROM studentPerformance sp " +
      "JOIN song s ON sp.SongID = s.ID " +
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

   public ResultSet getTimesQuizzedAndCorrect(int songID, int studentID, int playlistID) throws SQLException {
      String query = "SELECT TimesQuizzed, TimesCorrect FROM studentperformance WHERE songID = ? AND studentID = ? AND playlistID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, songID);
      pstmt.setInt(2, studentID);
      pstmt.setInt(3, playlistID);
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
      String query = "SELECT * " +
      "FROM studentPerformance " +      
      "WHERE StudentID = ? and PlaylistID = ? and SongID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, (Integer) studentID);
      pstmt.setInt(2, (Integer) playlistID);
      pstmt.setInt(3, (Integer) songID);
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
   public void updatePerformanceData(int timesQuizzed, int timesCorrect, double score, int studentPerformanceID) throws SQLException {
      String query = "UPDATE studentPerformance " +
                     "SET TimesQuizzed = ?, TimesCorrect = ?, Score = ? " +
                     "WHERE ID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, timesQuizzed);
      pstmt.setInt(2, timesCorrect);
      pstmt.setDouble(3, score);
      pstmt.setInt(4, studentPerformanceID);

      pstmt.executeUpdate();

   }

   public void addPerformanceData(int studentID, int songID, int playlistID, int timesQuizzed, int timesCorrect, double score) throws SQLException {
      String query = "INSERT INTO studentPerformance (StudentID, SongID, PlaylistID, TimesQuizzed, TimesCorrect, Score) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, studentID);
      pstmt.setInt(2, songID);
      pstmt.setInt(3, playlistID);
      pstmt.setInt(4, timesQuizzed);
      pstmt.setInt(5, timesCorrect);
      pstmt.setDouble(6, score);

      pstmt.executeUpdate();
   }

   public void deletePerformanceByPlaylistID(int classID) throws SQLException {
      String query = "DELETE FROM studentPerformance sp " + 
                     "WHERE sp.PlaylistID = (SELECT pl.ID FROM playlist pl, class c WHERE c.ID = ? AND pl.classID = c.ID)";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, classID);
      pstmt.executeUpdate();
   }
}
