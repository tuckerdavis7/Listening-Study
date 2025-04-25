package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ReportRepository;
import com.example.repositories.TeacherRepository;
import com.example.repositories.UserRepository;
import com.example.repositories.ClassRepository;
import com.sun.net.httpserver.HttpExchange;

public class AdministratorService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorService.class);
    UserRepository userRepository = new UserRepository();
    ReportRepository reportRepository = new ReportRepository();
    ClassRepository classRepository = new ClassRepository();
    TeacherRepository teacherRepository = new TeacherRepository();

    /**
     * Gathers the reports from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getAllReports(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = reportRepository.getAllReports();
            ArrayList<Map<String, Object>> reportList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> reportMap = new HashMap<>();
                reportMap.put("ID", result.getInt("ID"));
                reportMap.put("initialDate", result.getString("timeOfReport"));
                reportMap.put("modifiedDate", result.getString("lastUpdatedTime"));
                reportMap.put("modifiedBy", result.getString("lastUpdatedBy"));
                reportMap.put("description", result.getString("description"));
                reportMap.put("email", result.getString("email"));
                reportMap.put("status", result.getString("status"));
                reportMap.put("resolution", result.getString("resolution"));
                
                reportList.add(reportMap);
            }
            responseString = super.formatJSON(reportList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllReports of AdministratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

     /**
     * Gathers the users from the DB
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getAllUsers(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = userRepository.getAllUsers();
            ArrayList<Map<String, Object>> usersList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("email", result.getString("email"));
                userMap.put("firstName", result.getString("first_name"));
                userMap.put("lastName", result.getString("last_name"));
                userMap.put("role", result.getString("role"));
                userMap.put("id", result.getInt("user_id"));
                
                usersList.add(userMap);
            }
            responseString = super.formatJSON(usersList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllUsers of AdministratorService:");
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
            return responseString;
        }

        //remove teachers from any classes, checks for moderator since the role is already changed
        if (parameters.get("role").equals("moderator")) {
            try {
                classRepository.removeTeacherFromClasses(parameters);
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in updateDesignation (2) of AdministratorService:");
                e.printStackTrace();
                return responseString;
            }

            try {
                teacherRepository.removeTeacherFromClasses(parameters);
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in updateDesignation (3) of AdministratorService:");
                e.printStackTrace();
            }
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

    /**
     * Updates the status of a bug report
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of success or error message
     */
    public String updateReportStatus(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);
        Integer userID = super.getSessionUserID(exchange);

        try {
            ResultSet result = userRepository.getUserById(userID);
            if (result.next()) {
                parameters.put("lastUpdatedBy", result.getString("email").toString());
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateReport of AdministratorService:");
            e.printStackTrace();
            return responseString;
        }

        try {
            reportRepository.updateReportStatus(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateReport (2) of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }
}