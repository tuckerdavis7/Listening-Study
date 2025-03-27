package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

public class AdministratorUserService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorUserService.class);
    UserRepository userRepository = new UserRepository();

    public String getAllUsers(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = userRepository.getAllUsers();
            ArrayList<Map<String, Object>> usersList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("username", result.getString("username"));
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
            userRepository.updateDesignation(parameters);
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