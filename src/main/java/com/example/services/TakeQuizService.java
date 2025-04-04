package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.QuizSettingsRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.sun.net.httpserver.HttpExchange;

import com.example.implementations.TakeQuizImplementation;

/**
 * Service class for taking API requests, processing, and sending queries for the take quiz screen.
 */
public class TakeQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();
    PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();
    StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();
    TakeQuizImplementation takeQuizImplementation = new TakeQuizImplementation();

    /**
     * Gets the quiz settings for the given playlistID
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getQuizSettings(HttpExchange exchange) throws IOException {
        Map<String, Object> configParams = super.getQueryParameters(exchange);
        Object playlistID = configParams.get("playlistID"); //needs different id later
        String responseString = "";
        ArrayList<Map<String, Object>> quizSettingsList = new ArrayList<>();

        try {
            ResultSet result = quizSettingsRepository.getQuizSettingsByID(playlistID);
            
            while (result.next()) {
                Map<String, Object> quizSettingsMap = new HashMap<>();
                quizSettingsMap.put("playlistID", result.getInt("playlistID"));
                quizSettingsMap.put("playbackMethod", result.getString("playbackMethod"));
                quizSettingsMap.put("playbackDuration", result.getInt("playbackDuration"));
                quizSettingsMap.put("numQuestions", result.getString("numQuestions"));
                
                quizSettingsList.add(quizSettingsMap);
            }
            responseString = super.formatJSON(quizSettingsList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizSettings of TakeQuizService: " + e.getMessage());
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
    public String getSongs(HttpExchange exchange) throws IOException {
        Map<String, Object> songParams = super.getQueryParameters(exchange);
        int playlistID = ((Number)songParams.get("playlistID")).intValue();
        int studentID = ((Number)songParams.get("studentID")).intValue();
        ArrayList<Map<String, Object>> playlistSongList = new ArrayList<>();
        int numQuestions = 0;
        String responseString = "";

        try {
            ResultSet result = playlistSongRepository.getSongs(playlistID);

            while (result.next()) {
                Map<String, Object> playlistSongMap = new HashMap<>();
                playlistSongMap.put("playlistID", result.getInt("playlistID"));
                playlistSongMap.put("songID", result.getInt("songID"));
                playlistSongMap.put("udTimestamp", result.getInt("udTimestamp"));
                playlistSongMap.put("songName", result.getString("songName"));
                playlistSongMap.put("songComposer", result.getString("songComposer"));
                playlistSongMap.put("songYear", result.getInt("songYear"));
                playlistSongMap.put("youtubeLink", result.getString("youtubeLink"));
                playlistSongMap.put("mrTimestamp", result.getInt("mrTimestamp"));
                playlistSongMap.put("weight", 1.0);
                
                playlistSongList.add(playlistSongMap);
            }
            
        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs of TakeQuizService: " + e.getMessage());
            e.printStackTrace();
            return responseString;
        }

        try {
            ResultSet result = quizSettingsRepository.getQuizSettingsByID(playlistID);

            if (result.next()) {
                numQuestions = result.getInt("numQuestions");
            }

        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs of TakeQuizService: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            for (Map<String, Object> song : playlistSongList) {
                int songID = ((Number)song.get("songID")).intValue();
                ResultSet result = studentPerformanceRepository.getQuizWeights(songID, studentID);

                if (result.next()) {
                    double weight = result.getDouble("Weight");
                    song.put("weight", weight >= 0 ? weight : 1.0);
                }
            }

            if (playlistSongList.isEmpty()) {
                responseString = super.formatJSON("error", "No songs found.");
            }
            else {
                List<Map<String, Object>> selectedQuestions = takeQuizImplementation.getWeightedRandom(playlistSongList, numQuestions);
                responseString = super.formatJSON(selectedQuestions, "success");
            }

        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs (3) of TakeQuizService: " + e.getMessage());
            e.printStackTrace();
        }

        return responseString;
    }
}