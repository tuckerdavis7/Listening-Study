package com.example.services;

import java.io.IOException;
import java.util.Map;

import com.example.implementations.SnippetImplementation;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries for the student song snippets.
 */
public class SnippetService extends BaseService {
    private SnippetImplementation snippetImplementation = new SnippetImplementation();

      /**
     * Gets the timestamp for the song
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String timestamp for frontend
     */
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

    /**
     * Checks the boundary of the song snippet and playback duration
     *
     * @param songDuration length of the song
     * @param playBlackDuration lenght of the song playback
     * @param timeStamp timestamp the playback starts at
     * @return result of if the song plays outside of the boundary
     */
    private boolean checkTimeBoundary(int songDuration, int playbackDuration, int timestamp){
        return timestamp <= songDuration - playbackDuration;
    }
    
}
