package com.example.services.implementations;

import java.util.Random;

public class SnippetImplementation {
    public int generateRandomTimestamp(int songDuration){
        
        Random random = new Random();
        int timeStamp = random.nextInt(songDuration);

        return timeStamp;
    }

    public int refactorTimeStamp(int songDuration, int playbackduration){
        return songDuration - playbackduration; 
    }
    
}
