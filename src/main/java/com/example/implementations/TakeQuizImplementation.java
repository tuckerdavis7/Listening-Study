package com.example.implementations;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.math3.distribution.BetaDistribution;

/**
 * Implementation class for taking processing on the take quiz screen.
 */
public class TakeQuizImplementation {

    /**
     * Gets the list of random weights assigned to songs
     *
     * @param playlistSongList List of songs frmo the playlist
     * @param numQuestions Number of questions specified from the quiz settings
     * @return List Song details with weight included for frontend
     */
    public Map<String, Object> getThompsonSelection(ArrayList<Map<String, Object>> playlistSongList, ArrayList<Integer> alreadySelectedIDs) {
        double minSample = 1;
        Map<String, Object> selectedSong = null;

        for (Map<String, Object> song : playlistSongList) {
            int songID = ((Number) song.get("songID")).intValue();
            int previousSelectedSongID;
            if (alreadySelectedIDs.isEmpty())
                previousSelectedSongID = -1;
            else
                previousSelectedSongID = alreadySelectedIDs.get(alreadySelectedIDs.size() - 1);
            // If the song was just used, don't use it again
            if (alreadySelectedIDs.contains(songID) && songID == previousSelectedSongID) {
                continue;
            }

            int timesCorrect = ((Number) song.getOrDefault("timesCorrect", 0)).intValue();
            int timesQuizzed = ((Number) song.getOrDefault("timesQuizzed", 0)).intValue();
            double successRate = (timesQuizzed == 0) ? 0.0 : (timesCorrect / (double) timesQuizzed);

            // If the student is doing well and it was already used, skip it unless under threshold
            if (alreadySelectedIDs.contains(songID) && successRate >= 0.7) {
                continue;
            }

            // Beta distribution: alpha = successes + 1, beta = failures + 1
            double alpha = 1 + timesCorrect;
            double beta = 1 + (timesQuizzed - timesCorrect);

            // Handle songs with no quiz history
            if (timesQuizzed == 0) {
                alpha = 1.0;
                beta = 1.0;
            }

            double sample = new BetaDistribution(alpha, beta).sample();
            System.out.println(song.get("songName"));
            System.out.println(sample);
            if (sample < minSample) {
                minSample = sample;
                selectedSong = song;
                selectedSong.put("belief", sample);
            }
        }

        return selectedSong;
    }
}