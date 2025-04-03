package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.implementations.QuizImplementation;
import com.example.repositories.SongRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.sun.net.httpserver.HttpExchange;

public class QuizResultsService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(QuizResultsService.class);
    private SongRepository songRepository = new SongRepository();
    private StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();
    private QuizImplementation quizImplementation = new QuizImplementation();


    public String getQuizResults(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> songQuizData = new ArrayList<>();
        List<Map<String, Object>> quizData = super.getParametersList(exchange);
        String responseString ="";
        int numQuestions = quizData.size();
        
        for (int i = 0; i< numQuestions;i++){
            Map<String, Object> songData = new HashMap<>();
            try {
             ResultSet rs = songRepository.getSongData(((Number)quizData.get(i).get("songID")).intValue());             
             songData.put("name", rs.getString("songName"));
             songData.put("composer", rs.getString("songComposer"));
             songData.put("year", rs.getString("songYear"));
            } catch (SQLException e)
            {
                logger.error("Error in getting song data: " + e.getMessage());
       
            }
            List<Integer> performance = new ArrayList<>();
            try {
            ResultSet rs = studentPerformanceRepository.getPerformanceData(((Number)quizData.get(i).get("studentID")).intValue(), ((Number)quizData.get(i).get("playlistID")).intValue(), ((Number)quizData.get(i).get("songID")).intValue());             
             performance.add(rs.getInt("TimesQuizzed"));
             performance.add(rs.getInt("TimesCorrect"));
             performance.add(rs.getInt("ID"));
            } catch (SQLException e)
            {
                logger.error("Error in getting performance data: " + e.getMessage());       
            }
            int timesCorrect = performance.get(1); 
            
            if (quizImplementation.checkAnswers(quizData.get(i), songData)) {                
                timesCorrect+=1;                              
            }
            else {
                songQuizData.add(songData);
            }

            int timesQuizzed = performance.get(0) + 1;
            List<Double> weightRate = quizImplementation.calculateWeight(timesQuizzed, timesCorrect);
            
            try {
                studentPerformanceRepository.updatePerformanceData(timesQuizzed, timesCorrect, weightRate.get(0), weightRate.get(1), performance.get(3));
            } catch (SQLException e) {
                logger.error("Error in updating performance data: " + e.getMessage());  
            }
        }        
        responseString = super.formatJSON(songQuizData, "success");

        return responseString;
    }

    
}
