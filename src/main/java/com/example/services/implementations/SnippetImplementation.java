package com.example.services.implementations;

import java.util.Random;

public class SnippetImplementation {
    public int generateRandomTimestamp(int songDuration){
        
        Random random = new Random();
        int timestamp = random.nextInt(songDuration);

        return timestamp;
    }

    public int refactorTimeStamp(int songDuration, int playbackDuration){
        return songDuration - playbackDuration; 
    }
    
}
