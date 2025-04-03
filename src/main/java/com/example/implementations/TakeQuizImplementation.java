package com.example.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

public class TakeQuizImplementation {

    public List<Map<String, Object>> getWeightedRandom(ArrayList<Map<String, Object>> playlistSongList, int numQuestions) {
        List<Map<String, Object>> selectedQuestions = new ArrayList<>();
        Set<Integer> selectedSongIDs = new HashSet<>();

        Random rand = new Random();

        while (selectedQuestions.size() < numQuestions && !playlistSongList.isEmpty()) {
            double totalWeight = 0.0;
            for (Map<String, Object> song : playlistSongList) {
                if (!selectedSongIDs.contains(((Number)song.get("songID")).intValue())) {
                    int songID = ((Number) song.get("songID")).intValue();
                    if(!selectedSongIDs.contains(songID)) {
                        double weight = extractWeight(song.get("weight"));
                        totalWeight += (weight > 0) ? weight : 0.01;
                    }
                }
            }

            if (totalWeight == 0) {
                break;
            }
        

            double randomValue = rand.nextDouble() * totalWeight;
            double cumulativeWeight = 0.0;

            for (Map<String, Object> song : playlistSongList) {
                int songID = ((Number) song.get("songID")).intValue();
                if (selectedSongIDs.contains(songID)) {
                    continue;
                }

                double weight = extractWeight(song.get("weight"));
                cumulativeWeight += (weight > 0) ? weight : 0.01;

                cumulativeWeight += extractWeight(song.get("weight"));
                if (randomValue <= cumulativeWeight) {
                    selectedQuestions.add(song);
                    selectedSongIDs.add(songID);
                    break;
                }
            }
        }
        return selectedQuestions;
    }

    private double extractWeight(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            }
            catch (NumberFormatException e){
                throw new IllegalArgumentException("Invalid weight format: " + value);
            }
        }
        throw new IllegalArgumentException("Weight must be an Double or a String for a number: " + value);
    }
}