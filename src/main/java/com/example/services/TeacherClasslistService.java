package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ClassRepository;
import com.example.repositories.TeacherRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Teacher Classlist service for the viewing classlist.
 */
public class TeacherClasslistService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherClasslistService.class);
    private ClassRepository classlistRepository = new ClassRepository();
    private TeacherRepository teacherRepository = new TeacherRepository();
    
     /**
     * Gathers the classes from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getClasslist(HttpExchange exchange) throws IOException {
        int userID = super.getSessionUserID(exchange);
        int teacherID = -1;
        String responseString = "";

        try {
            ResultSet rs = teacherRepository.getTeacherID(userID);
            rs.next();
            teacherID = rs.getInt("ID"); 
        } catch (SQLException e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClasslist of TeacherClasslistService 1:");            
            e.printStackTrace();
            return responseString;
        }      
        
        try {
            ResultSet result = classlistRepository.getClasslist(teacherID);
            
            ArrayList<Map<String, Object>> classlist = new ArrayList<>();


            while (result.next()) {
                Map<String, Object> classMap = new HashMap<>();
                classMap.put("class_id", result.getInt("class_id"));
                classMap.put("classname", result.getString("classname"));
                classMap.put("students_count", result.getInt("students_count"));
                classMap.put("playlist_count", result.getInt("playlist_count"));
                
                classlist.add(classMap);
            }
            responseString = super.formatJSON(classlist, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClasslist of TeacherClasslistService 2:");
        }
        return responseString;
    }
    
}
