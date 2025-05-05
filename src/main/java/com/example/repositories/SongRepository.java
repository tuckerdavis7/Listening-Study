package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the song table.
 */
public class SongRepository {
      
     /**
     * Adds song and its data to the song table.
     *
     * @param songName The Name of the song to be added to the table
     * @param songComposer The Composer of the song to be added to the table.
     * @param songYear The year the song was released to be added to the table
     * @param youtubeLink The link to the song on youtube to be added to the table
     * @param mrTimestamp The most replayed timestamp to be added to the table
     * @throws SQLException When the query does not run properly
     */
    public void commitSongData(String songName,  String songComposer, String songYear, String youtubeLink, int mrTimestamp) throws SQLException {
        String query = "INSERT INTO song (songName, songComposer, songYear, youtubeLink, mrTimestamp) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, songName);
        pstmt.setString(2, songComposer);
        pstmt.setString(3, songYear);
        pstmt.setString(4, youtubeLink);
        pstmt.setInt(5, mrTimestamp);        
        pstmt.executeUpdate();
   
    }

     /**
     * Returns songID based on the youtubelink
     *
     * @param youtubeLink the youtube link of a song
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getSongID(String youtubeLink) throws SQLException {        
        String query = "SELECT ID FROM song WHERE youtubeLink =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        
        pstmt.setString(1, youtubeLink);
        ResultSet rs = pstmt.executeQuery();

        return rs;   
    }
    
    /**
     * Returns the songData by songID
     *
     * @param songID The ID of the song
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getSongData(int songID) throws SQLException {
        //List<String> songData = new ArrayList<>();        
        String query = "SELECT * FROM song WHERE ID =?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, songID);
        ResultSet rs = pstmt.executeQuery();

        //songData.add(rs.getString("songName"));
       // songData.add(rs.getString("songComposer"));
       // songData.add(rs.getString("songYear"));
        //songData.add(rs.getString("youtubeLink"));
        //songData.add(rs.getString("mrTimestamp"));
       
       return rs;   
    }

    public void updateSongData(String songName,  String songComposer, String songYear, String youtubeLink, int mrTimestamp, int songID) throws SQLException {
        String query = "UPDATE song SET songName = ?, songComposer = ?, songYear = ?, youtubeLink = ?, mrTimestamp = ? WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, songName);
        pstmt.setString(2, songComposer);
        pstmt.setString(3, songYear);
        pstmt.setString(4, youtubeLink);
        pstmt.setInt(5, mrTimestamp);
        pstmt.setInt(6, songID);
        pstmt.executeUpdate();
    }    
    
}
