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

public class TakeQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();

    public String getQuizSettings(HttpExchange exchange) throws IOException {
        Map<String, Object> playlistParams = super.getQueryParameters(exchange);
        Object playlistID = playlistParams.get("playlistID");
        String responseString = "";
        try {
            ResultSet result = quizSettingsRepository.getQuizSettingsByID(playlistID);
            ArrayList<Map<String, Object>> quizSettingsList = new ArrayList<>();
            
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

    // public String setQuizParameters(HttpExchange exchange) throws IOException {
    //     Map<String, Object> quizData = super.getParameters(exchange);

    //     int playlistID = Integer.parseInt((String)quizData.get("playlist"));
    //     String playbackMethod = (String)quizData.get("playbackMethod");
    //     int playbackDuration = Integer.parseInt((String)quizData.get("playbackDuration"));
    //     int numQuestions = Integer.parseInt((String)quizData.get("numQuestions"));
        
    //     String responseString = "";
    //     try {
    //         quizSettingsRepository.addQuizSettings(playlistID, playbackMethod, playbackDuration, numQuestions);
    //         responseString = super.formatJSON("success");
    //     }
    //     catch (Exception e) {
    //         responseString = "Internal Server Error";
    //         logger.error("Error in setQuizParameters of SetQuizService: " + e.getMessage());
    //     }
    //     return responseString;
    // }
}