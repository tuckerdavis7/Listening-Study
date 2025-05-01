package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.implementations.SongImplementation;
import com.example.repositories.PlaylistRepository;
import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.SongRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the teacher songs.
 */
public class TeacherSongService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherSongService.class);
    private SongRepository songRepository = new SongRepository();
    private PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();
    private PlaylistRepository playlistRepository = new PlaylistRepository();
    private SongImplementation songImplementation = new SongImplementation();

     /**
     * Adds a song to a playlist by sending its data
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String addSong(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getParameters(exchange);
        String playlistID = (String)songData.get("playlistID");
        String songName = (String)songData.get("name");
        String songComposer = (String)songData.get("composer");
        String songYear = (String)songData.get("year");
        String songURL = (String)songData.get("url");
        int userDefinedTimestamp = songImplementation.convertTimeToSeconds((String)songData.get("timestamp"));
        String songVideoID = songImplementation.extractVideoId(songURL);
        String responseString = "";

        int songID = -1;
        //Check if song exists already in song table
        try {
            ResultSet result = songRepository.getSongID(songVideoID);
            if (result.next())
                songID = result.getInt("ID");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong1 of TeacherSongService");
        }

        //If song does not exist in song table, add it
        try {
            if (songID == -1) {
                int songMostReplayedTimestamp = songImplementation.getMostReplayedTimestamp(songURL);
                songRepository.commitSongData(songName, songComposer, songYear, songVideoID, songMostReplayedTimestamp);
                ResultSet result = songRepository.getSongID(songVideoID);
                if (result.next())
                    songID = result.getInt("ID");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong2 of TeacherSongService");
        }

        //Add song to playlist
        try {
            playlistSongRepository.addToPlaylist(Integer.parseInt(playlistID), songID, userDefinedTimestamp);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong3 of TeacherSongService");
        }

        return responseString;
    }

     /**
     * Gets the playlist songs to view a playlist
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend with songs 
     */
    public String getPlaylistSongs(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getQueryParameters(exchange);
        int userID = super.getSessionUserID(exchange);
        int playlistID = ((Number)songData.get("playlistID")).intValue();
        String responseString = "";

        try {
            ResultSet result = playlistRepository.getPlaylistIDsForUser(userID);

            boolean found = false;
            while (result.next() && !found) {
                int pID = result.getInt("playlist_id");
                if (pID == playlistID) {
                    found = true;
                }
            }

            if (!found) {
                responseString = "Unauthorized";
                return responseString;
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getPlaylistSongs1 of TeacherSongService:");
        }
        try {
            ResultSet result = playlistSongRepository.getSongs(playlistID);

            ArrayList<Map<String, Object>> songList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> songMap = new HashMap<>();
                songMap.put("songID", result.getInt("songID"));
                songMap.put("name", result.getString("songName"));
                songMap.put("composer", result.getString("songComposer"));
                songMap.put("year", result.getString("songYear"));
                songMap.put("url", result.getString("youtubeLink"));
                songMap.put("mrTimestamp", result.getString("mrTimestamp"));
                songMap.put("udTimestamp", result.getString("udTimestamp"));
                
                songList.add(songMap);
            }
            responseString = super.formatJSON(songList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getPlaylistSongs2 of TeacherSongService:");
        }
        return responseString;
    }

    /**
     * Edits data in a song (name, composer, or year)
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String editSong(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getParameters(exchange);
        int songID = ((Number)songData.get("songID")).intValue();
        int playlistID = ((Number)songData.get("playlistID")).intValue();
        String songName = (String)songData.get("name");
        String songComposer = (String)songData.get("composer");
        String songYear = (String)songData.get("year");
        String songURL = (String)songData.get("url");
        int userDefinedTimestamp = songImplementation.convertTimeToSeconds((String)songData.get("timestamp"));
        String songVideoID = songImplementation.extractVideoId(songURL);
        String responseString = "";

        //Update song in songs table
        try {
            int songMostReplayedTimestamp = songImplementation.getMostReplayedTimestamp(songURL);
            songRepository.updateSongData(songName, songComposer, songYear, songVideoID, songMostReplayedTimestamp, songID);
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in editSong2 of TeacherSongService");
        }

        //Update song in playlist songs table
        try {
            playlistSongRepository.updatePlaylistSong(playlistID, songID, userDefinedTimestamp);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in editSong3 of TeacherSongService");
        }

        return responseString;
    }

    /**
     * Deletes a song from a playlist
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String deleteSong(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getParameters(exchange);
        int songID = ((Number)songData.get("songID")).intValue();
        int playlistID = ((Number)songData.get("playlistID")).intValue();
        String responseString = "";

        //Delete song in playlist songs table
        try {
            playlistSongRepository.deletePlaylistSong(playlistID, songID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in deleteSong of TeacherSongService");
        }

        return responseString;
    }
}
