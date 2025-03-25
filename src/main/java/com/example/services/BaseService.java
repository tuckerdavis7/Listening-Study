package com.example.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
public class BaseService {
    private Gson gson = new Gson();
    
    // Get multiple JSON objects as parameters
    protected List<Map<String, Object>> getParametersList(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        return gson.fromJson(requestBody, new TypeToken<List<Map<String, Object>>>(){}.getType());
    }
    
    // Get a single JSON object as parameter
    protected Map<String, Object> getParameters(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        return gson.fromJson(requestBody, new TypeToken<Map<String, Object>>(){}.getType());
    }
    
    // Helper method to read request body
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
    
    //return JSON-formatted string (GET)
    protected String formatJSON(Object data, String status) throws IOException {
        String responseString = "";
        Map<String, Object> responseMap = Map.of("status", status, "data", data);
        ObjectMapper objectMapper = new ObjectMapper();
        responseString = objectMapper.writeValueAsString(responseMap);
       
        return responseString;
    }
    
    //return JSON-formatted string (POST, UPDATE, DELETE) on success
    protected String formatJSON(String status) throws IOException {
        String responseString = "";
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put("status", status);
        responseString = objectMapper.writeValueAsString(status);
       
        return responseString;
    }

    //return JSON-formatted string (POST, UPDATE, DELETE) on failure
    protected String formatJSON(String status, String message) throws IOException {
        String responseString = "";
        Map<String, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map.put("status", status);
        map.put("message", message);
        responseString = objectMapper.writeValueAsString(map);
       
        return responseString;
    }
    
    //return last part of URL path for processing
    protected String getLastPathSegment(URI uri) {
        String path = uri.getPath();
        String[] segments = path.split("/");
        return segments.length > 0 ? segments[segments.length - 1] : null;
    }
}