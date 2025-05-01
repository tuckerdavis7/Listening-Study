package com.example.services;

import com.example.repositories.SessionRepository;
import com.example.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.net.httpserver.HttpExchange;

/**
 * Base Service class that contains methods to be used in other service classes.
 */
public class BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);
    private Gson gson = new Gson();
    SessionRepository sessionRepository = new SessionRepository();

    /**
     * Returns the user ID retrieved from the session.
     *
     * @param exchange The HTTP exchange containing the request and cookies
     * @return user ID retrieved using the session ID cookie
     * @throws IOException If reading the session cookie fails
     */
    public int getSessionUserID(HttpExchange exchange) throws IOException {
        String sessionID = CookieUtil.getCookieSessionID(exchange);

        try {
            int userID = sessionRepository.getUserIDBySessionID(sessionID);
            return userID;
        }
        catch (Exception e) {
            logger.error("Error in getSessionUserID of BaseService:");
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Compares the user's role (retrieved from session) with the role extracted from the URL.
     *
     * @param exchange The HTTP exchange containing the request and cookies
     * @param urlRole The role extracted from the URL path (e.g., "administrator", "student")
     * @return true if the user's role matches the role in the URL; false otherwise
     * @throws IOException If reading the session cookie fails
     */
    public boolean compareRoles(HttpExchange exchange, String urlRole) throws IOException{
        String sessionID = CookieUtil.getCookieSessionID(exchange);

        try {
            ResultSet result = sessionRepository.getUserRoleBySessionID(sessionID);
            if (result.next()) {
                String userRole = result.getString("role");
                if (userRole.equals(urlRole)) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            logger.error("Error in compareRoles of BaseService:");
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Takes in JSON body (POST, PATCH, DELETE) from request and returns multiple JSON objects
     *
     * @param exchange The data from the API request
     * @throws IOException If formatting operations fail
     * @return List of JSON objects to be used
     */
    public List<Map<String, Object>> getParametersList(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        return gson.fromJson(requestBody, new TypeToken<List<Map<String, Object>>>(){}.getType());
    }
    
    /**
     * Takes in JSON body (POST, PATCH, UPDATE) from request and returns a single JSON object
     *
     * @param exchange The data from the API request
     * @throws IOException If formatting operations fail
     * @return Map of singular JSON object to be used
     */
    public Map<String, Object> getParameters(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        return gson.fromJson(requestBody, new TypeToken<Map<String, Object>>(){}.getType());
    }

    /**
     * Takes in parameters from URL (GET requests) body from request and returns a single JSON object
     *
     * @param exchange The data from the API request
     * @throws IOException If formatting operations fail
     * @return Map of singular JSON object to be used
     */
    public Map<String, Object> getQueryParameters(HttpExchange exchange) {
        Map<String, Object> parameters = new HashMap<>();
        
        String query = exchange.getRequestURI().getQuery();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if (idx > 0) {
                    String key = pair.substring(0, idx);
                    String value = pair.substring(idx + 1);
                    
                    // Try to parse the value to a more specific type
                    try {
                        // Try to parse as Integer
                        parameters.put(key, Integer.parseInt(value));
                    } catch (NumberFormatException e1) {
                        try {
                            // Try to parse as Double
                            parameters.put(key, Double.parseDouble(value));
                        } catch (NumberFormatException e2) {
                            // If both number parsing attempts fail, treat it as a String
                            parameters.put(key, value);
                        }
                    }
                }
            }
        }
        
        return parameters;
    }
    
    /**
     * Helper method used to grab the request body for POST, PATCH, DELETE calls
     *
     * @param exchange The data from the API request
     * @throws IOException If the method is unable to read the request body
     * @return String formatted request body to be used in other methods
     */
    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
    
    /**
     * Returns a JSON-formatted string to frontend (GET)
     *
     * @param data The data to be returned to the frontend
     * @param status A status used by the UI (typically "success" or "failure")
     * @throws IOException If the method is unable to process and return the format
     * @return String formatted response body
     */
    public String formatJSON(Object data, String status) throws IOException {
        String responseString = "";
        Map<String, Object> responseMap = Map.of("status", status, "data", data);
        ObjectMapper objectMapper = new ObjectMapper();
        responseString = objectMapper.writeValueAsString(responseMap);
       
        return responseString;
    }
    
    /**
     * Returns a successful status message to frontend (POST, PATCH, DELETE)
     *
     * @param status The status used by the UI
     * @throws IOException If the method is unable to process and return the status
     * @return String formatted status
     */
    public String formatJSON(String status) throws IOException {
        String responseString = "";
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put("status", status);
        responseString = objectMapper.writeValueAsString(status);
       
        return responseString;
    }

    /**
     * Returns a failed status message to frontend (POST, PATCH, DELETE)
     *
     * @param status The status used by the UI
     * @param message The message to be returned with the status
     * @throws IOException If the method is unable to process and return the status
     * @return String formatted status
     */
    public String formatJSON(String status, String message) throws IOException {
        String responseString = "";
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put("status", status);
        map.put("message", message);
        responseString = objectMapper.writeValueAsString(map);
       
        return responseString;
    }
}