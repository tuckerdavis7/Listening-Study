package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistRepository;
import com.example.repositories.QuizSettingsRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the quiz settings page.
 */
public class SetQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(SetQuizService.class);
    PlaylistRepository playlistRepository = new PlaylistRepository();
    QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();

    /**
     * Gathers playlist data based on classID
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getPlaylists(HttpExchange exchange) throws IOException {
        Map<String, Object> playlistParams = super.getQueryParameters(exchange);
        Object classID = playlistParams.get("classID");
        String responseString = "";
        try {
            ResultSet result = playlistRepository.getPlaylistByClassID(classID);
            ArrayList<Map<String, Object>> playlistList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> playlistMap = new HashMap<>();
                playlistMap.put("ID", result.getInt("ID"));
                playlistMap.put("teacherID", result.getInt("teacherID"));
                playlistMap.put("classID", result.getInt("classID"));
                playlistMap.put("playlistName", result.getString("playlistName"));
                
                playlistList.add(playlistMap);
            }
            responseString = super.formatJSON(playlistList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getPlaylists of SetQuizService:");
            e.printStackTrace();
        }
        return responseString;
    }

    /**
     * Sends current quiz parameters to the database for later retrieval
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String setQuizParameters(HttpExchange exchange) throws IOException {
        Map<String, Object> quizData = super.getParameters(exchange);

        int playlistID = Integer.parseInt((String)quizData.get("playlist"));
        String playbackMethod = (String)quizData.get("playbackMethod");
        int playbackDuration = Integer.parseInt((String)quizData.get("playbackDuration"));
        int numQuestions = Integer.parseInt((String)quizData.get("numQuestions"));
        
        String responseString = "";
        try {
            quizSettingsRepository.addQuizSettings(playlistID, playbackMethod, playbackDuration, numQuestions);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in setQuizParameters of SetQuizService:");
            e.printStackTrace();
        }
        return responseString;
    }
}