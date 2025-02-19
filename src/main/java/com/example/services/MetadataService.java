package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.example.repositories.MetadataRepository;
import com.sun.net.httpserver.HttpExchange;

public class MetadataService extends BaseService{
     public String getMetadata(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = MetadataRepository.getApplicationMetadata();
            while (result.next()) {
                Map<String, Object> appDetailsMap = new HashMap<>();

                appDetailsMap.put("appName", result.getString("appName"));
                appDetailsMap.put("version", result.getDouble("version"));
                appDetailsMap.put("userCount", result.getInt("userCount"));
                appDetailsMap.put("lastUpdate", result.getString("lastUpdate"));
                appDetailsMap.put("logo", result.getString("logo"));

                responseString = formatJSON(appDetailsMap, "success");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
