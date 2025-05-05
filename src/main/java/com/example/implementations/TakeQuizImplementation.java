package com.example.implementations;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.math3.distribution.BetaDistribution;

/**
 * Implementation class for taking processing on the take quiz screen.
 */
public class TakeQuizImplementation {

    /**
     * Gets the belief of success for songs using Thompson Sampling.
     *
     * @param playlistSongList List of songs from the playlist
     * @param alreadySelectedIDs List of song IDs that have already been chosen for the quiz
     * @return Map of song details with belief included for the frontend and the playback method to be used based on belief
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
            // If the song was just used, don't use it again. If playlist has only 1 song, this doesn't apply.
            if (alreadySelectedIDs.contains(songID) && songID == previousSelectedSongID && playlistSongList.size() != 1) {
                continue;
            }

            int timesCorrect = ((Number) song.getOrDefault("timesCorrect", 0)).intValue();
            int timesQuizzed = ((Number) song.getOrDefault("timesQuizzed", 0)).intValue();

            // Beta distribution: alpha = successes + 1, beta = failures + 1
            double alpha = 1 + timesCorrect;
            double beta = 1 + (timesQuizzed - timesCorrect);

            // Handle songs with no quiz history
            if (timesQuizzed == 0) {
                alpha = 1.0;
                beta = 1.0;
            }

            double sample = new BetaDistribution(alpha, beta).sample();
            if (sample < minSample) {
                minSample = sample;
                selectedSong = song;
                selectedSong.put("belief", sample);
            }

        }

        //select playbackMethod based on minSample
        if (minSample > 0.9) {
            selectedSong.put("playbackMethod", "Random");
        }
        else if (minSample < 0.5) {
            selectedSong.put("playbackMethod", "MostReplayed");
        }
        else {
            selectedSong.put("playbackMethod", "Preferred");
        }

        return selectedSong;
    }
}