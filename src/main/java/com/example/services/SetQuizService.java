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
import com.example.repositories.QuizResultsRepository;
import com.example.repositories.SessionRepository;
import com.example.repositories.StudentClassRepository;
import com.example.repositories.StudentRepository;
import com.example.utils.CookieUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the quiz settings page.
 */
public class SetQuizService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(SetQuizService.class);
    private PlaylistRepository playlistRepository = new PlaylistRepository();
    private QuizSettingsRepository quizSettingsRepository = new QuizSettingsRepository();
    private QuizResultsRepository quizResultsRepository = new QuizResultsRepository();
    private SessionRepository sessionRepository = new SessionRepository();
    private StudentRepository studentRepository = new StudentRepository();
    private StudentClassRepository studentClassRepository = new StudentClassRepository();

    /**
     * Gathers playlist data based on classID
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getPlaylists(HttpExchange exchange) throws IOException {
        /*Map<String, Object> playlistParams = super.getQueryParameters(exchange);
        Object classID = playlistParams.get("classID");*/

        String responseString = "";
        try {
            String sessionID = CookieUtil.getCookieSessionID(exchange);
            int userID = sessionRepository.getUserIDBySessionID(sessionID);
            int studentID = -1;
            ArrayList<Integer> classIDs = new ArrayList<>();

            /*if(userID == null) {
                responseString = super.formatJSON("Invalid or expired session", "error");
                return responseString;
            }*/

            ResultSet rs = studentRepository.getStudentByUserID(userID);
            if (rs.next()) {
                studentID = rs.getInt("ID");
                System.out.println("StudentID: " + studentID);
            }

            ResultSet rs1 = studentClassRepository.getClassIDByStudentID(studentID);
            while (rs1.next()) {
                int temp = rs1.getInt("classID");
                classIDs.add(temp);
                //System.out.println("StudentID: " + studentID);
            }

            ResultSet result = playlistRepository.getPlaylistByClassID(classIDs);
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
        int userID = super.getSessionUserID(exchange); 
        int playlistID = Integer.parseInt((String) quizData.get("playlistID"));
               
        String playbackMethod = (String)quizData.get("playbackMethod");
        int playbackDuration = Integer.parseInt((String)quizData.get("playbackDuration"));
        int numQuestions = Integer.parseInt((String)quizData.get("numQuestions"));
        
        String responseString = "";
        try {
            quizSettingsRepository.setDeletedByID(userID);
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in setQuizParameters1 of SetQuizService:");
            e.printStackTrace();
        }
        try {
            quizResultsRepository.setDeletedByID(userID);
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in setQuizParameters2 of SetQuizService:");
            e.printStackTrace();
        }

        try {
            quizSettingsRepository.addQuizSettings(userID, playbackMethod, playbackDuration, numQuestions, playlistID);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in setQuizParameters3 of SetQuizService:");
            e.printStackTrace();
        }
        return responseString;
    }
}