package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.implementations.SongImplementation;
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
        int songMostReplayedTimestamp = songImplementation.getMostReplayedTimestamp(songURL);
        String songVideoID = songImplementation.extractVideoId(songURL);
        String responseString = "";
        try {
            songRepository.commitSongData(songName, songComposer, songYear, songVideoID, songMostReplayedTimestamp);
            logger.info("3");
            ResultSet result = songRepository.getSongID(songVideoID);
            int songID = 0;
            if (result.next())
                songID = result.getInt("ID");

            playlistSongRepository.addToPlaylist(Integer.parseInt(playlistID), songID, userDefinedTimestamp);

            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong of TeacherSongService:");
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
        int playlistID = ((Number)songData.get("playlistID")).intValue();
        
        String responseString = "";
        try {
            ResultSet result = playlistSongRepository.getSongs(playlistID);

            ArrayList<Map<String, Object>> songList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> songMap = new HashMap<>();
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
            logger.error("Error in getPlaylistSongs of TeacherSongService:");
        }
        return responseString;
    }
}
