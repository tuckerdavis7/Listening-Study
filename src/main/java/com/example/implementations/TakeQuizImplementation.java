package com.example.implementations;

import org.apache.commons.math3.distribution.BetaDistribution;
import java.util.*;
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
    public List<Map<String, Object>> getThompsonSample(ArrayList<Map<String, Object>> playlistSongList, int numQuestions) {
        List<Map<String, Object>> selectedQuestions = new ArrayList<>();
        Set<Integer> selectedSongIDs = new HashSet<>();

        Random rand = new Random();

        while(selectedQuestions.size() < numQuestions && !playlistSongList.isEmpty()) {
            String selectedSongID = null;
            double maxSample = -1;
            Map<String, Object> selectedSong = null;

            for (Map<String,Object> song : playlistSongList) {
                int songID = ((Number) song.get("songID")).intValue();
                int timesCorrect = ((Number) song.getOrDefault("timesCorrect", 0)).intValue();
                int timesQuizzed = ((Number) song.getOrDefault("timesQuizzed", 0)).intValue();

                boolean alreadyAsked = selectedSongIDs.contains(songID);
                double successRate = (timesQuizzed == 0) ? 0.0 : (timesCorrect / (double) timesQuizzed);

                if(alreadyAsked && successRate >= 0.7) {
                    continue;
                }

                double alpha = 1 + (timesQuizzed - timesCorrect);
                double beta = 1 + timesCorrect;
                BetaDistribution betaDist = new BetaDistribution(alpha, beta);
                double sample = betaDist.sample();

                if(sample > maxSample) {
                    maxSample = sample;
                    selectedSongID = String.valueOf(songID);
                    selectedSong = song;
                }
            }

            if(selectedSong != null) {
                selectedQuestions.add(selectedSong);
                int selectedID = ((Number) selectedSong.get("songID")).intValue();
                selectedSongIDs.add(selectedID);
            }
            else {
                break;
            }
        }
        return selectedQuestions;
    }
}