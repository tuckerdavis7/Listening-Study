package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.configuration.DatabaseConfiguration;

public class PlaylistSongRepository {
    public List<Integer> getSongIDs(int playListID) throws SQLException {
        List<Integer> songIDs = new ArrayList<>();
        String query = "SELECT songID FROM playlistsongs WHERE playlistID =?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playListID);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            songIDs.add(rs.getInt("songID"));
        }

       return songIDs;   
    }

    public List<Integer> getSongTimestamps(int playListID, int playbackMethod) throws SQLException {        
        List<Integer> timeStamps = new ArrayList<>();

        switch (playbackMethod) {
            case 1:
            {                
                return timeStamps;
            }
            case 2:
            {
                String query = "SELECT timestamp FROM playlistsongs WHERE playlistID =?";
                PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
                pstmt.setInt(1, playListID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    timeStamps.add(rs.getInt("songID"));
                }
                return timeStamps;
            }
            case 3:
            {
                String query = "SELECT userDefinedtimestamp FROM playlistsongs WHERE playlistID =?";
                PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
                pstmt.setInt(1, playListID);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    timeStamps.add(rs.getInt("songID"));
                }
                return timeStamps;
            }
            default:
                return timeStamps;  
        }   
    }

    public void addToPlaylist(int playListID, int songID, int userDefinedTimestamp) throws SQLException {
        String query = "INSERT INTO playlistsongs (playlistID, songID, udTimeStamp) VALUES (?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playListID);
        pstmt.setInt(2, songID);
        pstmt.setInt(3, userDefinedTimestamp);        
        pstmt.executeUpdate();
   
    }

    public ResultSet getSongs(int playListID) throws SQLException {
        String query = "SELECT s.ID AS songID, s.songName, s.songComposer, s.songYear, s.youtubeLink, s.mrTimestamp, ps.udTimestamp FROM playlistSongs ps JOIN song s ON ps.songID = s.ID WHERE ps.playlistID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playListID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
