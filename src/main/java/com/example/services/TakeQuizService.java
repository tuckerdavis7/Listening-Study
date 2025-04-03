package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.QuizSettingsRepository;
import com.sun.net.httpserver.HttpExchange;

public class TakeQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TakeQuizService.class);
    QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();
    PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();

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

    public String getSongs(HttpExchange exchange) throws IOException {
        Map<String, Object> songParams = super.getQueryParameters(exchange);
        int playlistID = ((Number)songParams.get("playlistID")).intValue();
        String responseString = "";

         try {
            ResultSet result = playlistSongRepository.getSongs(playlistID);
            ArrayList<Map<String, Object>> playlistSongList = new ArrayList<>();

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
                
                playlistSongList.add(playlistSongMap);
            }
            responseString = super.formatJSON(playlistSongList, "success");
        } 
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongs of TakeQuizService: " + e.getMessage());
            e.printStackTrace();

        }

        return responseString;
    }
}