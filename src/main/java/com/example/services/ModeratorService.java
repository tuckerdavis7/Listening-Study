package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ClassRepository;
import com.example.repositories.StudentRepository;
import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

public class ModeratorService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(ModeratorService.class);
    ClassRepository classRepository = new ClassRepository();
    UserRepository userRepository = new UserRepository();
    StudentRepository studentRepository = new StudentRepository();

     /**
     * Gathers the classes from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getAllClasses(HttpExchange exchange) throws IOException {
        logger.info("at getAllClasses in ModeratorService");
        String responseString = "";
        try {
            ResultSet result = classRepository.getAllClasses();
            ArrayList<Map<String, Object>> classList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classMap = new HashMap<>();
                classMap.put("classID", result.getInt("ID"));
                classMap.put("className", result.getString("className"));
                classMap.put("teacherID", result.getInt("teacherID"));
                
                classList.add(classMap);
            }
            responseString = super.formatJSON(classList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

     /**
     * Updates the designation of a teacher or moderator
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of success or error message
     */
    public String updateDesignation(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            userRepository.updateModeratorOrTeacherDesignation(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateDesignation of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    /**
     * Deletes (marks for deletion) a user
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of success or error message
     */
    public String deleteUser(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            userRepository.deleteUser(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in deleteUser of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    public String getClassTeachers(HttpExchange exchange) throws IOException {
        logger.info("at getClassTeachers in ModeratorService");
        Map<String, Object> classData = super.getQueryParameters(exchange);
        int classID = ((Number)classData.get("classID")).intValue();
        String responseString = "";
        try {
            ResultSet result = classRepository.getTeachersByClassID(classID);
            ArrayList<Map<String, Object>> classTeachers = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classTeachersMap = new HashMap<>();
                classTeachersMap.put("teacherID", result.getInt("ID"));
                classTeachersMap.put("firstName", result.getString("FirstName"));
                classTeachersMap.put("lastName", result.getString("LastName"));
                classTeachersMap.put("email", result.getString("Email"));
                
                classTeachers.add(classTeachersMap);
            }
            responseString = super.formatJSON(classTeachers, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

    public String getClassStudents(HttpExchange exchange) throws IOException {
        logger.info("at getClassStudents in ModeratorService");
        Map<String, Object> classData = super.getQueryParameters(exchange);
        int classID = ((Number)classData.get("classID")).intValue();
        String responseString = "";
        try {
            ResultSet result = studentRepository.getStudentRoster(classID);
            ArrayList<Map<String, Object>> classTeachers = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> classTeachersMap = new HashMap<>();
                classTeachersMap.put("studentID", result.getInt("ID"));
                classTeachersMap.put("firstName", result.getString("FirstName"));
                classTeachersMap.put("lastName", result.getString("LastName"));
                classTeachersMap.put("email", result.getString("Email"));
                
                classTeachers.add(classTeachersMap);
            }
            responseString = super.formatJSON(classTeachers, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllClasses of ModeratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

    
}
