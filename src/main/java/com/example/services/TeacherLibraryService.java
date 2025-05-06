package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistRepository;
import com.example.repositories.SessionRepository;
import com.example.repositories.StudentRepository;
import com.example.repositories.TeacherRepository;
import com.example.utils.CookieUtil;
import com.sun.net.httpserver.HttpExchange;

/**
 * Teacher library service for the viewing a teachers playlist library.
 */
public class TeacherLibraryService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherRosterService.class);
    private PlaylistRepository playlistRepository = new PlaylistRepository();
    private TeacherRepository teacherRepository = new TeacherRepository();
    private StudentRepository studentRepository = new StudentRepository();
    private SessionRepository sessionRepository = new SessionRepository();
    
     /**
     * Gathers the playlists and their associated class from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getLibrary(HttpExchange exchange) throws IOException {        
        String sessionID = CookieUtil.getCookieSessionID(exchange);
        int teacherID = -1;
        int studentID = -1;
        String responseString = "";
        String userRole = "";
        int userID = super.getSessionUserID(exchange);

        try {
            ResultSet rs = sessionRepository.getUserRoleBySessionID(sessionID);
            rs.next();
            userRole = rs.getString("Role");
        } catch (Exception e){
            e.printStackTrace();
            responseString = "Internal Server Error";
            logger.error("Error in getLibrary of TeacherLibraryService:");            
            return responseString;
        }
        
       
        try {
            if ("teacher".equals(userRole)) {
                ResultSet rs = teacherRepository.getTeacherID(userID);
                rs.next();
                teacherID = rs.getInt("ID"); 
            } else if ("student".equals(userRole)){
                ResultSet rs = studentRepository.getStudentByUserID(userID);
                rs.next();
                studentID = rs.getInt("ID"); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            responseString = "Internal Server Error";
            logger.error("Error in getLibrary of TeacherLibraryService:");            
            return responseString;
        }               
               
        try {
            ResultSet result = null;
            if (teacherID != -1){
                result = playlistRepository.getPlaylistByTeacherID(teacherID);
            } else if (studentID != -1){
                result = playlistRepository.getPlaylistByStudentID(studentID);
            }

            
            ArrayList<Map<String, Object>> playlistLibrary = new ArrayList<>();

            while (result.next()) {
                Map<String, Object> playlistMap = new HashMap<>();
                playlistMap.put("playlistID", result.getInt("ID"));
                playlistMap.put("playlistName", result.getString("playlistName"));
                playlistMap.put("className", result.getString("className"));
                
                playlistLibrary.add(playlistMap);
            }
            responseString = super.formatJSON(playlistLibrary, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getLibrary of TeacherLibraryService:");
            e.printStackTrace();
        }
        return responseString;
    }

    
     /**
     * Renames a selected playlist.
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String renamePlaylist(HttpExchange exchange) throws IOException { 
        String responseString = "";
        Map<String, Object> playlistParams = super.getQueryParameters(exchange);
        Object playlistID = playlistParams.get("playlistID");
        Object newPlaylistName = playlistParams.get("newName");
                
        try {
            playlistRepository.renamePlaylist(playlistID, newPlaylistName);            
        } catch (SQLException e) {
            responseString = "Internal Server Error";
            logger.error("Error in renamePlaylist of TeacherLibraryService:");
        }

        return responseString;
    }
    
}
