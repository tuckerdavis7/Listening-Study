package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class SongRepository {
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

    public ResultSet getSongID(String youtubeLink) throws SQLException {        
        String query = "SELECT ID FROM song WHERE youtubeLink =?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        
        pstmt.setString(1, youtubeLink);
        ResultSet rs = pstmt.executeQuery();

        return rs;   
    }

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
    
}
