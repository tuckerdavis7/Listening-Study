package com.example.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation class for extra processing of quizzes.
 */
public class QuizImplementation {
     /**
     * Checks the answers to see if the student got the question correct
     *
     * @param quizData Map student inputted data for question
     * @param Songdata map is actually answer of song question
     * @return true or false if the answer matches the question
     */
    public boolean checkAnswers(Map<String, Object> quizData, Map<String, Object> songData) {
        if (((String)quizData.get("name")).toLowerCase().equals(((String)songData.get("name")).toLowerCase()) &&
            ((String)quizData.get("composer")).toLowerCase().equals(((String)songData.get("composer")).toLowerCase()) &&
            ((String)quizData.get("year")).toLowerCase().equals(((String)songData.get("year")).toLowerCase()))
            return true;
        return false;
    } 

     /**
     *Calculated the weight new weight of the question
     * 
     * @param timesQuizzed times the question was quizzed
     * @param timesCorrect map is actually answer of song question
     * @return true or false if the answer matches the question
     */
    public List<Double> calculateWeight(int timesQuizzed, int timesCorrect){
        double weight = 1 - ((double)timesCorrect / timesQuizzed);
        double score = ((double)timesCorrect / timesQuizzed) * 100;
        List<Double> weightScore = new ArrayList<>();
        weightScore.add(weight);
        weightScore.add(score); 
        return weightScore;
    } 
}
