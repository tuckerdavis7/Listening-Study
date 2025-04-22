package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistRepository;
import com.sun.net.httpserver.HttpExchange;

public class TeacherLibraryService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherRosterService.class);
    private PlaylistRepository playlistRepository = new PlaylistRepository();
    
    public String getLibrary(HttpExchange exchange) throws IOException {
        Map<String, Object> teacherData = super.getQueryParameters(exchange);
        int teacherID = ((Number)teacherData.get("teacherID")).intValue();       

        
        String responseString = "";
        try {
            ResultSet result = playlistRepository.getPlaylistByTeacherID(teacherID);
            
            ArrayList<Map<String, Object>> playlistLibrary = new ArrayList<>();

            while (result.next()) {
                Map<String, Object> playlistMap = new HashMap<>();
                playlistMap.put("ID", result.getInt("ID"));
                playlistMap.put("playlistName", result.getString("playlistName"));
                playlistMap.put("className", result.getString("className"));
                
                playlistLibrary.add(playlistMap);
            }
            responseString = super.formatJSON(playlistLibrary, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getClasslist of TeacherRosterService:");
        }
        return responseString;
    }
    
}
