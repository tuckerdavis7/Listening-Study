package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.implementations.TakeQuizImplementation;
import com.example.repositories.PlaylistRepository;
import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.QuizSettingsRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.example.repositories.StudentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the take quiz screen.
 */
public class TakeQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();
    PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();
    StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();
    StudentRepository studentRepository = new StudentRepository();
    PlaylistRepository playlistRepository = new PlaylistRepository();
    TakeQuizImplementation takeQuizImplementation = new TakeQuizImplementation();

    /**
     * Gets the quiz settings for the given playlistID
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getQuizSettings(HttpExchange exchange) throws IOException {
        int userID = super.getSessionUserID(exchange);
        String responseString = "";
        ArrayList<Map<String, Object>> quizSettingsList = new ArrayList<>();

        try {
            ResultSet result = quizSettingsRepository.getQuizSettingsByID(userID);
            
            while (result.next()) {
                Map<String, Object> quizSettingsMap = new HashMap<>();
                quizSettingsMap.put("userID", result.getInt("user_id"));
                quizSettingsMap.put("playbackMethod", result.getString("playbackMethod"));
                quizSettingsMap.put("playbackDuration", result.getInt("playbackDuration"));
                quizSettingsMap.put("numQuestions", result.getString("numQuestions"));
                quizSettingsMap.put("quizSettingsID", result.getInt("ID"));
                
                quizSettingsList.add(quizSettingsMap);
            }
            responseString = super.formatJSON(quizSettingsList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizSettings of TakeQuizService:");
            e.printStackTrace();
        }

        return responseString;
    }

    /**
     * Gets the songs from the given playlistID
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String nextQuestionSong(HttpExchange exchange) throws IOException {
        Map<String, Object> params = super.getParameters(exchange);
        int userID = super.getSessionUserID(exchange);
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Integer> alreadySelectedIDs = mapper.convertValue(
            params.get("previous"),
            new TypeReference<ArrayList<Integer>>() {}
        );

        ArrayList<Map<String, Object>> playlistSongList = new ArrayList<>();
        int studentID = -1;
        int playlistID = -1;
        String playbackMethod = "";
        String responseString = "";

        //Convert user ID to student ID
        try {
            ResultSet result = studentRepository.getStudentByUserID(userID);
            
            if (result.next()) {
                studentID = result.getInt("ID");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            e.printStackTrace();
            logger.error("Error in getSongs1 of TakeQuizService");
            return responseString;
        }

        //Get playlist ID from user's active quiz settings
        try {
            ResultSet result = quizSettingsRepository.getQuizSettingsByID(userID);

            if (result.next()) {
                playlistID = result.getInt("playlistID");
                playbackMethod = result.getString("playbackMethod");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            e.printStackTrace();
            logger.error("Error in getSongs2 of TakeQuizService");
            return responseString;
        }

        // Get all songs from playlist
        try {
            ResultSet result = playlistSongRepository.getSongs(playlistID);

            while (result.next()) {
                Map<String, Object> playlistSongMap = new HashMap<>();
                playlistSongMap.put("playlistID", result.getInt("playlistID"));
                playlistSongMap.put("songID", result.getInt("songID"));
                playlistSongMap.put("songName", result.getString("songName"));
                playlistSongMap.put("songComposer", result.getString("songComposer"));
                playlistSongMap.put("songYear", result.getInt("songYear"));
                playlistSongMap.put("youtubeLink", result.getString("youtubeLink"));           
                playlistSongMap.put("mrTimestamp", result.getInt("mrTimestamp"));
                playlistSongMap.put("udTimestamp", result.getInt("udTimestamp"));
                
                playlistSongList.add(playlistSongMap);
            }
        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs3 of TakeQuizService:");
            e.printStackTrace();
            return responseString;
        }

        //Get playlist name from playlist repository using playlistID
        try {
            ResultSet result = playlistRepository.getPlaylistNameByID((Integer)playlistSongList.get(0).get("playlistID"));
            String playlistName = "Playlist";
            if (result.next()) {
                playlistName = result.getString("playlistName");
            }
            for (Map<String, Object> song : playlistSongList) {
                song.put("playlistName", playlistName);
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs4 of TakeQuizService:");
            e.printStackTrace();
            return responseString;
        }

        //Append times correct and times quizzed to song data
        try {
            for (Map<String, Object> song : playlistSongList) {
                int songID = ((Number)song.get("songID")).intValue();
                ResultSet result = studentPerformanceRepository.getTimesQuizzedAndCorrect(songID, studentID, playlistID);
                int timesCorrect = 0;
                int timesQuizzed = 0;
                if (result.next()) {
                    timesCorrect = result.getInt("TimesCorrect");
                    timesQuizzed = result.getInt("TimesQuizzed");
                }
                song.put("timesCorrect", timesCorrect);
                song.put("timesQuizzed", timesQuizzed);
                
            }
        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs5 of TakeQuizService:");
            e.printStackTrace();
        }

        //Select song using Thompson Sampling model
        try {
            if (playlistSongList.isEmpty()) {
                responseString = super.formatJSON("error", "No songs found.");
            }
            else {
                Map<String, Object> selectedSong = takeQuizImplementation.getThompsonSelection(playlistSongList, alreadySelectedIDs);
                if (((String)selectedSong.get("playbackMethod")).equals("Random")) {
                    selectedSong.put("timestamp", -1);
                }
                else if (((String)selectedSong.get("playbackMethod")).equals("MostReplayed")) {
                    selectedSong.put("timestamp", selectedSong.get("mrTimestamp"));
                }
                else { // Preferred
                    if (playbackMethod.equals("MostReplayed")) {
                        selectedSong.put("timestamp", selectedSong.get("mrTimestamp"));
                    }
                    else if (playbackMethod.equals("TeacherTimestamp")) {
                        selectedSong.put("timestamp", selectedSong.get("udTimestamp"));
                    }
                    else { // Preferred is Random
                        selectedSong.put("timestamp", -1);
                    }
                }
                selectedSong.remove("udTimestamp");
                selectedSong.remove("mrTimestamp");

                responseString = super.formatJSON(selectedSong, "success");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs6 of TakeQuizService:");
            e.printStackTrace();
        }

        return responseString;
    }
} 