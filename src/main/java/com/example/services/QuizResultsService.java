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
import com.example.repositories.PlaylistRepository;
import com.example.repositories.QuizResultsRepository;
import com.example.repositories.QuizSettingsRepository;
import com.example.repositories.SongRepository;
import com.example.repositories.StudentPerformanceRepository;
import com.example.repositories.StudentRepository;
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
    private StudentRepository studentRepository = new StudentRepository();
    private PlaylistRepository playlistRepository = new PlaylistRepository();
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
        int userID = super.getSessionUserID(exchange);
        int studentID = -1;

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
            logger.error("Error in setQuizResults1 of QuizResultsService");
            return responseString;
        }

        for (int i = 0; i< numQuestions;i++) {
            Map<String, Object> songData = new HashMap<>();
            int playlistID = 0;
            int songID = 0;
            try {
                songID = Integer.parseInt((String)quizData.get(i).get("songID"));
                ResultSet rs = songRepository.getSongData(songID);
                playlistID = Integer.parseInt((String)quizData.get(i).get("playlistID"));
                if (rs.next()) {
                    songData.put("name", rs.getString("songName"));
                    songData.put("composer", rs.getString("songComposer"));
                    songData.put("year", rs.getString("songYear"));
                }
            }
            catch (SQLException e)
            {
                responseString = "Internal Server Error";
                e.printStackTrace();
                logger.error("Error in setQuizResults2 (" + Integer.toString(i) + ") of QuizResultsService");
                return responseString;
       
            }

            List<Integer> performance = new ArrayList<>();
            try {
                ResultSet rs = studentPerformanceRepository.getPerformanceData(studentID, playlistID, songID);             
                if (rs.next()) {
                    performance.add(rs.getInt("TimesQuizzed"));
                    performance.add(rs.getInt("TimesCorrect"));
                    performance.add(rs.getInt("ID"));
                }
            }
            catch (SQLException e)
            {
                responseString = "Internal Server Error";
                e.printStackTrace();
                logger.error("Error in setQuizResults3 of QuizResultsService");
                return responseString;
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
                quizData.get(i).replace("songID", songID);
                songQuizData.add(quizData.get(i));
            }

            int timesQuizzed = 1;
            if (quizzedBefore) {
                timesQuizzed = performance.get(0) + 1;
            }
            
            try {
                if (quizzedBefore)
                    studentPerformanceRepository.updatePerformanceData(timesQuizzed, timesCorrect, (double)timesCorrect/timesQuizzed * 100, performance.get(2));
                else 
                    studentPerformanceRepository.addPerformanceData(studentID, songID, playlistID, timesQuizzed, timesCorrect, (double)timesCorrect/timesQuizzed * 100);
            }
            catch (SQLException e) {
                responseString = "Internal Server Error";
                e.printStackTrace();
                logger.error("Error in setQuizResults4 of QuizResultsService");
                return responseString;
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
        int userID = super.getSessionUserID(exchange);
        
        String responseString = "";
        for (int i = 0; i < quizData.size(); i++) {
            int quizSettingsID = Integer.parseInt((String)quizData.get(i).get("quizSettingsID"));
            String songName = (String)(quizData.get(i).get("name"));
            String songComposer = (String)(quizData.get(i).get("composer"));
            String songYear = (String)(quizData.get(i).get("year"));
            int songID = ((Double)quizData.get(i).get("songID")).intValue();
            int numQuestions = Integer.parseInt((String)quizData.get(i).get("numQuestions"));
            
            try {
                quizResultsRepository.addQuizResults(quizSettingsID, songName, songComposer, songYear, songID, userID, numQuestions);
            }
            catch (Exception e) {
                logger.error("Error in forwarding quiz results:");
                e.printStackTrace();
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
        // Map<String, Object> configParams = super.getQueryParameters(exchange);
        // Object quizSettingsID = configParams.get("quizSettingsID");
        int userID = super.getSessionUserID(exchange);

        String responseString = "";
        ArrayList<Map<String, Object>> quizResultsList = new ArrayList<>();

        try {
            ResultSet result = quizResultsRepository.getQuizResults(userID);
            
            while (result.next()) {
                Map<String, Object> quizResultsMap = new HashMap<>();
                quizResultsMap.put("songID", result.getInt("songID"));
                quizResultsMap.put("name", result.getString("songName"));
                quizResultsMap.put("composer", result.getString("songComposer"));
                quizResultsMap.put("year", result.getString("songYear"));
                
                quizResultsList.add(quizResultsMap);
            }
            responseString = super.formatJSON(quizResultsList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizResults1 of QuizResultsService");
            e.printStackTrace();
        }

        return responseString;
    }

    public String getQuizSettings(HttpExchange exchange) throws IOException {
        int userID = super.getSessionUserID(exchange);
        String responseString = "";
        Map<String, Object> quizSettingsMap = new HashMap<String,Object>();
        try {
            
            ResultSet rs = quizSettingsRepository.getQuizSettings(userID);

            if (rs.next()) {
                quizSettingsMap.put("quizSettingsID", rs.getInt("ID"));
                quizSettingsMap.put("numQuestions", rs.getInt("numQuestions"));
                quizSettingsMap.put("playlistID", rs.getInt("playlistID"));
                quizSettingsMap.put("playbackMethod", rs.getString("playbackMethod"));
                quizSettingsMap.put("playbackDuration", rs.getString("playbackDuration"));
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizSettings1 of QuizResultsService");
            e.printStackTrace();
        }

        
        try {
            ResultSet rs = playlistRepository.getPlaylistNameByID((Integer)quizSettingsMap.get("playlistID"));

            if (rs.next()) {
                quizSettingsMap.put("playlistName", rs.getString("playlistName"));
            }
            
            responseString = super.formatJSON(quizSettingsMap, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getQuizSettings2 of QuizResultsService");
            e.printStackTrace();
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
                logger.error("Error in getCorrectAnswers1 of QuizResultsService");
                e.printStackTrace();
            }

            try {
                quizResultsRepository.setDeletedByID(((Number)songIDobj).intValue());
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers2 of QuizResultsService:");
                e.printStackTrace();
            }

            try {
                quizSettingsRepository.setDeletedByID(((Number)songIDobj).intValue());
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers3 of QuizResultsService");
                e.printStackTrace();
            }

        }

        return responseString;
    }



    
}
