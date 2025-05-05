package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the playlistsongrepository table.
 */
public class PlaylistSongRepository {
    /**
     * Returns the songIDs from a specific playlist.
     *
     * @param playListID The ID of the active playlist
     * @throws SQLException When the query does not run properly
     * @return List of all songIDs in the tabls
     */
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

    /**
     * Returns the timeStamps to be used by the quiz
     *
     * @param playListID The ID of the active playlist
     * @param playbackMethod The playbackmethod type random, teacherdefined, or most viewed
     * @throws SQLException When the query does not run properly
     * @return timestamp, returns empty if random for 1, most viewed for 2, and userdefined for 3
     */
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

    /**
     * Adds a SongID and its user-defined timestamp to a playlist
     *
     * @param playListID The ID of the playlist being inserted into
     * @param songID The ID of the song getting added to the playlist
     * @param userDefinedTimestamp The timestamp defined by the teacher added to the table.
     * @throws SQLException When the query does not run properly
     */
    public void addToPlaylist(int playListID, int songID, int userDefinedTimestamp) throws SQLException {
        String query = "INSERT INTO playlistsongs (playlistID, songID, udTimeStamp) VALUES (?, ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playListID);
        pstmt.setInt(2, songID);
        pstmt.setInt(3, userDefinedTimestamp);        
        pstmt.executeUpdate();
   
    }

    /**
     * Returns the resultset containing the song data.
     *
     * @param playlistID The ID of the active playlist
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getSongs(int playlistID) throws SQLException {
        String query = "SELECT s.ID AS songID, s.songName, s.songComposer, s.songYear, s.youtubeLink, s.mrTimestamp, ps.playlistID, ps.udTimestamp FROM playlistSongs ps JOIN song s ON ps.songID = s.ID WHERE ps.playlistID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playlistID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Returns the resultset containing the playlist song data.
     *
     * @param playlistID The ID of the active playlist
     * @param songID The ID of the active song
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getPlaylistSongs(int playlistID, int songID) throws SQLException {
        String query = "SELECT * FROM playlistsongs WHERE playlistID = ? and songID = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playlistID);
        pstmt.setInt(2, songID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    public void updatePlaylistSong(int playlistID, int songID, int udTimestamp) throws SQLException {
        String query = "UPDATE playlistsongs SET udTimestamp = ? WHERE playlistID = ? and songID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, udTimestamp);
        pstmt.setInt(2, playlistID);
        pstmt.setInt(3, songID);
        pstmt.executeUpdate();
    }

    public void deletePlaylistSong(int playlistID, int songID) throws SQLException {
        String query = "DELETE FROM playlistsongs WHERE playlistID = ? and songID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setInt(1, playlistID);
        pstmt.setInt(2, songID);
        pstmt.executeUpdate();
    }

    public void deleteSongsByPlaylistID(int classID) throws SQLException {
        String query = "DELETE FROM playlistsongs ps " + 
                       "WHERE ps.playlistID = (SELECT pl.ID FROM playlist pl, class c WHERE c.ID = ? AND pl.classID = c.ID)";
  
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, classID);
        pstmt.executeUpdate();
     }
}
