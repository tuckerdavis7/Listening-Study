package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.StudentPerformanceRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the student performance screen.
 */
public class StudentPerformanceService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(StudentPerformanceService.class);
    StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();

    /**
     * Gets the performance of songs for a student
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getSongPerformances(HttpExchange exchange) throws IOException {
        Map<String, Object> performanceParams = super.getQueryParameters(exchange);
        Object studentID = performanceParams.get("studentID");
        String responseString = "";
        try {
            ResultSet result = studentPerformanceRepository.getPerformanceByID(studentID);
            ArrayList<Map<String, Object>> performanceList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> performanceMap = new HashMap<>();
                performanceMap.put("ID", result.getInt("ID"));
                performanceMap.put("studentID", result.getInt("StudentID"));
                performanceMap.put("score", result.getDouble("Score"));
                performanceMap.put("SongID", result.getInt("SongID"));
                performanceMap.put("timesCorrect", result.getInt("TimesCorrect"));
                performanceMap.put("timesQuizzed", result.getInt("TimesQuizzed"));
                performanceMap.put("songName", result.getString("songName"));
                performanceMap.put("composer", result.getString("songComposer"));
                performanceMap.put("year", result.getString("songYear"));
                performanceMap.put("url", result.getString("youtubeLink"));
                performanceMap.put("playlistName", result.getString("playlistName"));
                
                performanceList.add(performanceMap);
            }
            responseString = super.formatJSON(performanceList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getSongPerformances of StudentPerformanceService:");
            e.printStackTrace();
        }
        return responseString;
    }
}