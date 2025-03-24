package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.MetadataRepository;
import com.sun.net.httpserver.HttpExchange;
public class MetadataService extends BaseService {
        private static final Logger logger = LoggerFactory.getLogger(MetadataService.class);
    MetadataRepository metadataRepository = new MetadataRepository();

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
            logger.error("Error in getMetadata of MetadataService: " + e.getMessage());
        }
        return responseString;
    }
}