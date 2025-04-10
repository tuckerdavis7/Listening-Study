package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.MetadataRepository;
import com.sun.net.httpserver.HttpExchange;
/**
 * Service class for taking API requests, processing, and sending queries related to project metadata.
 */
public class MetadataService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    MetadataRepository metadataRepository = new MetadataRepository();

    /**
     * Returns the most recent metadata for the application
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String getMetadata(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = metadataRepository.getApplicationMetadata();
            while (result.next()) {
                Map<String, Object> appDetailsMap = new HashMap<>();
                appDetailsMap.put("appName", result.getString("appName"));
                appDetailsMap.put("version", result.getDouble("version"));
                appDetailsMap.put("userCount", result.getInt("userCount"));
                appDetailsMap.put("lastUpdate", result.getString("lastUpdate"));
                appDetailsMap.put("logo", result.getString("logo"));
                responseString = super.formatJSON(appDetailsMap, "success");
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getMetadata of MetadataService:");
            e.printStackTrace();
        }
        return responseString;
    }
}