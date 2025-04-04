package com.example.implementations;

import java.util.Random;

/**
 * Implementation class for extra processing of snippets.
 */
public class SnippetImplementation {
     /**
     * Generates a random timestamp for the song and teturns
     *
     * @param Song duration length of song
     * @return timestamp randomely generated
     */
    public int generateRandomTimestamp(int songDuration){
        
        Random random = new Random();
        int timeStamp = random.nextInt(songDuration);

        return timeStamp;
    }

     /**
     * Generates a random timestamp for the song and returns
     *
     * @param Song duration length of song
     * @param Playback duration length song playback
     * @return refractored timestamp
     */
    public int refactorTimeStamp(int songDuration, int playbackduration){
        return songDuration - playbackduration; 
    }
    
}
