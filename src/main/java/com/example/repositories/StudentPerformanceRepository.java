package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class StudentPerformanceRepository {
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

   public ResultSet getQuizWeights(int songID, int studentID) throws SQLException {
      String query = "SELECT Weight FROM studentPerformance WHERE songID = ? AND studentID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, (Integer) songID);
      pstmt.setInt(2, (Integer) studentID);
      ResultSet rs = pstmt.executeQuery();

      return rs;
   }

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

   public void updatePerformanceData(int timesQuizzed, int timesCorrect, double weight, double score, int ID) throws SQLException {
      String query = "UPDATE studentPerformance " +
                     "SET weight = ?, score = ?, timesQuizzed = ?, timesCorrect = ? " +
                     "WHERE ID = ?";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setDouble(1, weight);
      pstmt.setDouble(2, score);
      pstmt.setInt(3, timesQuizzed);
      pstmt.setInt(4, timesCorrect);
      pstmt.setInt(5, ID);

      pstmt.executeUpdate();

   }

   public void addPerformanceData(int studentID, int classID, Double weight, Double score, int songID, int playlistID, int timesQuizzed, int timesCorrect) throws SQLException {
      String query = "INSERT INTO studentPerformance (StudentID, ClassID, Weight, Score, SongID, PlaylistID, TimesCorrect, TimesQuizzed) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
      pstmt.setInt(1, studentID);
      pstmt.setInt(2, classID);
      pstmt.setDouble(3, weight);
      pstmt.setDouble(4, score);
      pstmt.setInt(5, songID);
      pstmt.setInt(6, playlistID);
      pstmt.setInt(7, timesQuizzed);
      pstmt.setInt(8, timesCorrect);

      pstmt.executeUpdate();
   }
}
