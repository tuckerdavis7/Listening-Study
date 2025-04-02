package com.example.services;

import java.io.IOException;
import java.util.Map;

import com.example.services.implementations.SnippetImplementation;
import com.sun.net.httpserver.HttpExchange;

//int playbackMethod, int playbackduration, int songDuration, int timeStamp
public class SnippetService extends BaseService {
    private SnippetImplementation snippetImplementation = new SnippetImplementation();
    public String getTimeStamp(HttpExchange exchange) throws IOException {      
        Map<String, Object> songData = super.getParameters(exchange);
        
        int playbackMethod = ((Number)songData.get("playbackMethod")).intValue();
        int playbackDuration = ((Number)songData.get("playbackDuration")).intValue();
        int songDuration = ((Number)songData.get("songDuration")).intValue();
        int timeStamp = ((Number)songData.get("timeStamp")).intValue();

        if (playbackMethod == 1 || timeStamp == -1) {
            timeStamp = snippetImplementation.generateRandomTimestamp(songDuration);
        }
 
        boolean boundaryCheck = checkTimeBoundary(songDuration, playbackDuration, timeStamp);
            
         if(!boundaryCheck){
            timeStamp = snippetImplementation.refactorTimeStamp(songDuration, playbackDuration);
         }
        
        return Integer.toString(timeStamp);
    } 

    public boolean checkTimeBoundary(int songDuration, int playbackduration, int timeStamp){
        return timeStamp <= songDuration - playbackduration;
    }
    
}
