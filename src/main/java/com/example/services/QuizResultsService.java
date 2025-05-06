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
     * Processes a submitted answer for a quiz question and updates the student performance data.
     * If the answer is incorrect, adds the result to the quiz results database.
     *
     * @param exchange The HTTP exchange containing quiz answer data
     * @return String JSON formatted response indicating success or error
     * @throws IOException If there is an error processing the request data
     */
    public String submitAnswer(HttpExchange exchange) throws IOException {
        Map<String, Object> quizData = super.getParameters(exchange);
        String responseString = "";
        int userID = super.getSessionUserID(exchange);
        int numQuestions = Integer.parseInt((String)quizData.get("numQuestions"));
        int studentID = 0;

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
            logger.error("Error in submitAnswer1 of QuizResultsService");
            return responseString;
        }

        Map<String, Object> songData = new HashMap<>();
        int playlistID = 0;
        int songID = 0;
        try {
            songID = Integer.parseInt((String)quizData.get("songID"));
            playlistID = Integer.parseInt((String)quizData.get("playlistID"));
            ResultSet rs = songRepository.getSongData(songID);
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
            logger.error("Error in submitAnswer2 of QuizResultsService");
            return responseString;
        }

        Map<String, Integer> performanceMap = new HashMap<>();
        try {
            ResultSet rs = studentPerformanceRepository.getPerformanceData(studentID, playlistID, songID);             
            if (rs.next()) {
                performanceMap.put("timesQuizzed", rs.getInt("TimesQuizzed"));
                performanceMap.put("timesCorrect", rs.getInt("TimesCorrect"));
                performanceMap.put("ID", rs.getInt("ID"));
            }
        }
        catch (SQLException e)
        {
            responseString = "Internal Server Error";
            e.printStackTrace();
            logger.error("Error in submitAnswer3 of QuizResultsService");
            return responseString;
        }

        boolean quizzedBefore = performanceMap.size() != 0;
        
        int timesCorrect = 0;
        int timesQuizzed = 1;
        if (quizzedBefore) {
            timesCorrect = performanceMap.get("timesCorrect");
            timesQuizzed = performanceMap.get("timesQuizzed") + 1;
        }

        if (quizImplementation.checkAnswers(quizData, songData)) { // if answer is correct
            timesCorrect += 1;
        }
        else { // if answer is wrong

            try {
                String songName = (String)quizData.get("name");
                String songComposer = (String)quizData.get("composer");
                String songYear = (String)quizData.get("year");
                int quizSettingsID = Integer.parseInt((String)quizData.get("quizSettingsID"));
                quizResultsRepository.addQuizResults(quizSettingsID, songName, songComposer, songYear, songID, userID, numQuestions);
            }
            catch (Exception e) {
                logger.error("Error in submitAnswer4 of QuizResultsService");
                e.printStackTrace();
                responseString = "Internal Server Error";
            }
        }

        try {
            if (quizzedBefore)
                studentPerformanceRepository.updatePerformanceData(timesQuizzed, timesCorrect, (double)timesCorrect/timesQuizzed * 100, performanceMap.get("ID"));
            else 
                studentPerformanceRepository.addPerformanceData(studentID, songID, playlistID, timesQuizzed, timesCorrect, (double)timesCorrect/timesQuizzed * 100);
            
            responseString = super.formatJSON("success");
        }
        catch (SQLException e) {
            responseString = "Internal Server Error";
            e.printStackTrace();
            logger.error("Error in setQuizResults4 of QuizResultsService");
            return responseString;
        }     

        return responseString;
    }

    /**
     * Returns quiz results to the quiz results page for the current user
     *
     * @param exchange The HTTP exchange containing request data
     * @return String JSON formatted string of quiz results data for frontend
     * @throws IOException If data processing fails
     */
    public String getQuizResults(HttpExchange exchange) throws IOException {
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

    /**
     * Retrieves the quiz settings for the current user
     *
     * @param exchange The HTTP exchange containing request data
     * @return String JSON formatted string of quiz settings data for frontend
     * @throws IOException If data processing fails
     */
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
     * Returns the correct answers for songs from a taken quiz and marks the quiz results 
     * and settings as deleted in the database
     *
     * @param exchange The HTTP exchange containing list of song IDs
     * @return String JSON formatted string of correct song data for frontend
     * @throws IOException If data processing fails
     */
    public String getCorrectAnswers(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> songIDs = super.getParametersList(exchange);
        List<Map<String, Object>> songData = new ArrayList<>();
        int userID = super.getSessionUserID(exchange);

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
                quizResultsRepository.setDeletedByID(userID);
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in getCorrectAnswers2 of QuizResultsService:");
                e.printStackTrace();
            }

            try {
                quizSettingsRepository.setDeletedByID(userID);
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