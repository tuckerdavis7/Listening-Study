package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.StudentRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Teacher roster service for the students in a class.
 */
public class TeacherRosterService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherRosterService.class);
    private StudentRepository studentRepository = new StudentRepository();

    /**
     * Gathers the students in a class from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getClassRoster(HttpExchange exchange) throws IOException {
        Map<String, Object> classData = super.getQueryParameters(exchange);
        int classID = ((Number)classData.get("classID")).intValue();       

        
        String responseString = "";
        try {
            ResultSet result = studentRepository.getStudentRoster(classID);
            
            ArrayList<Map<String, Object>> classRoster = new ArrayList<>();


            while (result.next()) {
                Map<String, Object> studentMap = new HashMap<>();
                studentMap.put("ID", result.getInt("ID"));
                studentMap.put("Firstname", result.getString("Firstname"));
                studentMap.put("LastName", result.getString("LastName"));
                studentMap.put("Email", result.getString("Email"));
                
                classRoster.add(studentMap);
            }
            responseString = super.formatJSON(classRoster, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClasslist of TeacherRosterService:");
        }
        return responseString;
    }
    
}
