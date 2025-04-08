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
import com.example.repositories.QuizResultsRepository;
import com.example.repositories.QuizSettingsRepository;
import com.example.repositories.SongRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the quiz results page.
 */
public class QuizResultsService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(QuizResultsService.class);
    private SongRepository songRepository = new SongRepository();
    private StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();
    private QuizResultsRepository quizResultsRepository = new QuizResultsRepository();
    private QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();
    private QuizImplementation quizImplementation = new QuizImplementation();

    /**
     * Sets quiz results in the database and calculates results
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String setQuizResults(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> quizData = super.getParametersList(exchange);
        List<Map<String, Object>> songQuizData = new ArrayList<>();
        String responseString ="";
        int numQuestions = quizData.size();

        for (int i = 0; i< numQuestions;i++) {
            Map<String, Object> songData = new HashMap<>();
            int playlistID = 0;
            int songID = 0;
            try {
                ResultSet rs = songRepository.getSongData(((Number)quizData.get(i).get("songID")).intValue());
                playlistID = ((Number)quizData.get(i).get("playlistID")).intValue();
                songID = ((Number)quizData.get(i).get("songID")).intValue();
                if (rs.next()) {
                    songData.put("name", rs.getString("songName"));
                    songData.put("composer", rs.getString("songComposer"));
                    songData.put("year", rs.getString("songYear"));
                }
            }
            catch (SQLException e)
            {
                logger.error("Error in getting song data: " + e.getMessage());
       
            }

            List<Integer> performance = new ArrayList<>();
            try {
                ResultSet rs = studentPerformanceRepository.getPerformanceData(1, playlistID, songID);             
                if (rs.next()) {
                    performance.add(rs.getInt("TimesQuizzed"));
                    performance.add(rs.getInt("TimesCorrect"));
                    performance.add(rs.getInt("ID"));
                }
            }
            catch (SQLException e)
            {
                logger.error("Error in getting performance data: " + e.getMessage());       
            }
            
            boolean quizzedBefore = performance.size() != 0;
            
            int timesCorrect = 0;
            if (quizzedBefore) {
                timesCorrect = performance.get(1); 
            }

            if (quizImplementation.checkAnswers(quizData.get(i), songData)) {
                timesCorrect += 1;
            }
            else {
                quizData.get(i).remove("playlistID");
                quizData.get(i).replace("songID", ((Number)(quizData.get(i).get("songID"))).intValue());
                songQuizData.add(quizData.get(i));
            }

            int timesQuizzed = 1;
            if (quizzedBefore) {
                timesQuizzed = performance.get(0) + 1;
            }
            
            List<Double> weightScore = quizImplementation.calculateWeight(timesQuizzed, timesCorrect);
            try {
                if (quizzedBefore)
                    studentPerformanceRepository.updatePerformanceData(timesQuizzed, timesCorrect, weightScore.get(0), weightScore.get(1), performance.get(2));
                else 
                    studentPerformanceRepository.addPerformanceData(1, 1, weightScore.get(0), weightScore.get(1), songID, playlistID, timesQuizzed, timesCorrect);
            }
            catch (SQLException e) {
                logger.error("Error in setting quiz results: " + e.getMessage());
                responseString = "Internal Server Error";
            }
        }        
        responseString = super.formatJSON(songQuizData, "success");

        return responseString;
    }

    /**
     * Forwards quiz results to the quiz results page
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String forwardQuizResults(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> quizData = super.getParametersList(exchange);

        String responseString = "";
        for (int i = 0; i < quizData.size(); i++) {
            int quizSettingsID = ((Number)quizData.get(i).get("quizSettingsID")).intValue();
            String songName = (String)(quizData.get(i).get("name"));
            String songComposer = (String)(quizData.get(i).get("composer"));
            String songYear = (String)(quizData.get(i).get("year"));
            int songID = ((Number)quizData.get(i).get("songID")).intValue();
        
            try {
                quizResultsRepository.addQuizResults(quizSettingsID, songName, songComposer, songYear, songID);
            }
            catch (Exception e) {
                logger.error("Error in forwarding quiz results: " + e.getMessage());
                responseString = "Internal Server Error";
            }
        }
        responseString = super.formatJSON( "success");

        return responseString;
    }

    /**
     * Returns quiz results to the quiz results page
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getQuizResults(HttpExchange exchange) throws IOException {
        Map<String, Object> configParams = super.getQueryParameters(exchange);
        Object quizSettingsID = configParams.get("quizSettingsID");

        String responseString = "";
        ArrayList<Map<String, Object>> quizResultsList = new ArrayList<>();

        try {
            ResultSet result = quizResultsRepository.getQuizResultsByID(quizSettingsID);
            
            while (result.next()) {
                Map<String, Object> quizResultsMap = new HashMap<>();
                quizResultsMap.put("songID", result.getInt("songID"));
                quizResultsMap.put("name", result.getString("songName"));
                quizResultsMap.put("composer", result.getString("songComposer"));
                quizResultsMap.put("year", result.getString("songYear"));
                
                quizResultsList.add(quizResultsMap);
            }
            logger.info(quizResultsList.toString());
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizResults1 of QuizResultsService: " + e.getMessage());
        }

        try {
            Map<String, Object> numQuestionsMap = new HashMap<String,Object>();
            ResultSet rs = quizSettingsRepository.getNumQuestionsByID(quizSettingsID);

            if (rs.next()) {
                numQuestionsMap.put("numQuestions", rs.getInt("numQuestions"));
            }
            quizResultsList.add(numQuestionsMap);
            logger.info(quizResultsList.toString());

            responseString = super.formatJSON(quizResultsList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizResults2 of QuizResultsService: " + e.getMessage());
        }

        return responseString;
    }

    /**
     * Returns the correct answers from the taken quiz on the quiz results page
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getCorrectAnswers(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> songIDs = super.getParametersList(exchange);
        List<Map<String, Object>> songData = new ArrayList<>();

        String responseString = "";
        
        for (int i = 0; i < songIDs.size(); i++) {
            Object songIDobj = songIDs.get(i).get("songID");
            try {
                ResultSet rs = songRepository.getSongData(((Number)songIDobj).intValue());

                Map<String, Object> song = new HashMap<>();
                if (rs.next()) {
                    song.put("name", rs.getString("songName"));
                    song.put("composer", rs.getString("songComposer"));
                    song.put("year", rs.getString("songYear"));
                }
                songData.add(song);

                responseString = super.formatJSON(songData, "success");
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers1 of QuizResultsService: " + e.getMessage());
            }

            try {
                quizResultsRepository.setDeletedByID(((Number)songIDobj).intValue());
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers2 of QuizResultsService: " + e.getMessage());
            }

            try {
                quizSettingsRepository.setDeletedByID(((Number)songIDobj).intValue());
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers3 of QuizResultsService: " + e.getMessage());
            }

        }

        return responseString;
    }



    
}
