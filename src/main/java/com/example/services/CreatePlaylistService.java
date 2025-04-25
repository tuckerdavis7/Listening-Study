package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.implementations.SongImplementation;
import com.example.repositories.ClassRepository;
import com.example.repositories.PlaylistRepository;
import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.SongRepository;
import com.example.repositories.TeacherRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the creation of playlists.
 */
public class CreatePlaylistService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherSongService.class);
    private TeacherRepository teacherRepository = new TeacherRepository();
    private ClassRepository classRepository = new ClassRepository();
    private SongRepository songRepository = new SongRepository();
    private PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();
    private PlaylistRepository playlistRepository = new PlaylistRepository();
    private SongImplementation songImplementation = new SongImplementation();

     /**
     * Gets class options for the create playlist page
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getClassOptions(HttpExchange exchange) throws IOException {
        int userID = super.getSessionUserID(exchange);
        int teacherID = -1;
        String responseString = "";

        //Get teacher ID based on user ID from session
        try {
            ResultSet result = teacherRepository.getTeacherID(userID);
            if (result.next()) {
                teacherID = result.getInt("ID");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClassOptions1 of CreatePlaylistService");
            e.printStackTrace();
        }

        //Get classes based on teacher ID
        ArrayList<Map<String, String>> classOptions = new ArrayList<>();
        try {
            ResultSet result = classRepository.getClasslist(teacherID);
            while (result.next()) {
                Map<String, String> classInfo = new HashMap<>();
                classInfo.put("classID", result.getString("class_id"));
                classInfo.put("className", result.getString("classname"));
                classOptions.add(classInfo);
            }
            responseString = super.formatJSON(classOptions, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClassOptions2 of CreatePlaylistService");
            e.printStackTrace();
        }

        return responseString;
    }

     /**
     * Creates playlist and adds songs to it
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String createPlaylist(HttpExchange exchange) throws IOException {
        Map<String, Object> playlistData = super.getParameters(exchange);
        String responseString = "";
        int userID = super.getSessionUserID(exchange);
        int teacherID = -1;
        int classID = -1;
        String playlistName = "";
        try {
            classID = Integer.parseInt((String)playlistData.get("classID"));
            playlistName = (String)playlistData.get("playlistName");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Map<String, String>> songData = mapper.convertValue(
            playlistData.get("songData"),
            new TypeReference<ArrayList<Map<String, String>>>() {}
        );
        //Get teacher ID based on user ID from session
        try {
            ResultSet result = teacherRepository.getTeacherID(userID);
            if (result.next()) {
                teacherID = result.getInt("ID");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in createPlaylist1 of CreatePlaylistService");
            e.printStackTrace();
            return responseString;
        }

        if (teacherID == -1 || classID == -1) {
            responseString = "Internal Server Error";
            return responseString;
        }
        
        // Create playlist in playlist table
        try {
            playlistRepository.createPlaylist(playlistName, teacherID, classID);
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in createPlaylist2 of CreatePlaylistService");
            e.printStackTrace();
            return responseString;
        }

        // Get newly created playlist ID
        int playlistID = -1;
        try {
            ResultSet result = playlistRepository.getPlaylistID(teacherID, classID);
            if (result.next()) {
                playlistID = result.getInt("ID");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in createPlaylist3 of CreatePlaylistService");
            e.printStackTrace();
            return responseString;
        }

        // Loop to add every song
        for (int i = 0; i < songData.size(); i++) {
            String songName = songData.get(i).get("name");
            String songComposer = songData.get(i).get("composer");
            String songURL = songData.get(i).get("youtubeLink");
            String songYear = songData.get(i).get("year");
            String songTimestamp = songData.get(i).get("timestamp");

            int userDefinedTimestamp = songImplementation.convertTimeToSeconds(songTimestamp);
            String songVideoID = songImplementation.extractVideoId(songURL);

            int songID = -1;
            //Check if song exists already in song table
            try {
                ResultSet result = songRepository.getSongID(songVideoID);
                if (result.next())
                    songID = result.getInt("ID");
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in createPlaylist3 of CreatePlaylistService");
                e.printStackTrace();
                return responseString;
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
                logger.error("Error in createPlaylist4 of CreatePlaylistService");
                e.printStackTrace();
                return responseString;
            }

            //Add song to playlist
            try {
                playlistSongRepository.addToPlaylist(playlistID, songID, userDefinedTimestamp);
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in createPlaylist5 of CreatePlaylistService");
                e.printStackTrace();
                return responseString;
            }
        }
        responseString = super.formatJSON("success");

        return responseString;
    }
}