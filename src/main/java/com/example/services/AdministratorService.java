package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ReportRepository;
import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

public class AdministratorService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorService.class);
    UserRepository userRepository = new UserRepository();
    ReportRepository reportRepository = new ReportRepository();

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
                
                reportList.add(reportMap);
            }
            responseString = super.formatJSON(reportList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllReports of AdministratorReportService: " + e.getMessage());
        }
        return responseString;
    }

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
                
                usersList.add(userMap);
            }
            responseString = super.formatJSON(usersList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllUsers of AdministratorUserService: " + e.getMessage());
        }
        return responseString;
    }

    public String updateDesignation(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            userRepository.updateModeratorOrTeacherDesignation(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateDesignation of AdministratorUserService: " + e.getMessage());
        }

        return responseString;
    }

    public String deleteUser(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            userRepository.deleteUser(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in deleteUser of AdministratorUserService: " + e.getMessage());
        }

        return responseString;
    }
}