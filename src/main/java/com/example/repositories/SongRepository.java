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
     * Adds a new song and its data to the song table.
     *
     * @param songName The name of the song
     * @param songComposer The composer of the song
     * @param songYear The year the song was released
     * @param youtubeLink The YouTube link of the song
     * @param mrTimestamp The most replayed timestamp of the song
     * @throws SQLException When the query does not run properly
     */
    public void commitSongData(String songName, String songComposer, String songYear, String youtubeLink, int mrTimestamp) throws SQLException {
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
     * Retrieves the song ID based on the YouTube link.
     *
     * @param youtubeLink The YouTube link of the song
     * @return ResultSet containing the song ID if found
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getSongID(String youtubeLink) throws SQLException {        
        String query = "SELECT ID FROM song WHERE youtubeLink = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        
        pstmt.setString(1, youtubeLink);
        ResultSet rs = pstmt.executeQuery();
        return rs;   
    }
    
    /**
     * Retrieves all song data based on the song ID.
     *
     * @param songID The ID of the song
     * @return ResultSet containing the song's data
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getSongData(int songID) throws SQLException {
        String query = "SELECT * FROM song WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, songID);
        ResultSet rs = pstmt.executeQuery();
        return rs;   
    }

    /**
     * Updates an existing song entry in the song table.
     *
     * @param songName The new name of the song
     * @param songComposer The new composer of the song
     * @param songYear The new year of the song
     * @param youtubeLink The updated YouTube link
     * @param mrTimestamp The updated most replayed timestamp
     * @param songID The ID of the song to be updated
     * @throws SQLException When the update query fails
     */
    public void updateSongData(String songName, String songComposer, String songYear, String youtubeLink, int mrTimestamp, int songID) throws SQLException {
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
