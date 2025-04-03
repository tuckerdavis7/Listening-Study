package com.example.services;

import java.io.IOException;
import java.util.Map;

import com.example.services.implementations.SnippetImplementation;
import com.sun.net.httpserver.HttpExchange;

public class SnippetService extends BaseService {
    private SnippetImplementation snippetImplementation = new SnippetImplementation();

    public String getTimeStamp(HttpExchange exchange) throws IOException {      
        Map<String, Object> songData = super.getQueryParameters(exchange);
        String playbackMethod = (String)songData.get("playbackMethod");
        int playbackDuration = ((Number)songData.get("playbackDuration")).intValue();
        int songDuration = ((Number)songData.get("songDuration")).intValue();
        int timestamp = ((Number)songData.get("timestamp")).intValue();
        
        if (playbackMethod.equals("Random") || timestamp == -1) {
            timestamp = snippetImplementation.generateRandomTimestamp(songDuration);
        }
 
        boolean boundaryCheck = checkTimeBoundary(songDuration, playbackDuration, timestamp);
            
         if(!boundaryCheck){
            timestamp = snippetImplementation.refactorTimeStamp(songDuration, playbackDuration);
         }
        
        return Integer.toString(timestamp);
    } 

    private boolean checkTimeBoundary(int songDuration, int playbackDuration, int timestamp){
        return timestamp <= songDuration - playbackDuration;
    }
    
}
