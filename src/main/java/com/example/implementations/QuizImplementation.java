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
    public boolean checkAnswers(Map<String, Object> quizData, Map<String, Object> songData){
        return !(quizData.get("name") != songData.get("name") ||quizData.get("composer") != songData.get("composer") || quizData.get("year") != songData.get("year"));
    } 

     /**
     *Calculated the weight new weight of the question
     * 
     * @param timesQuizzed times the question was quizzed
     * @param timesCorrect map is actually answer of song question
     * @return true or false if the answer matches the question
     */
    public List<Double> calculateWeight(int timesQuizzed, int timesCorrect){
        double weight = 1 - (timesCorrect/timesQuizzed);
        double rate = timesCorrect/timesQuizzed * 100;
        List<Double> weightRate = new ArrayList<>();
        weightRate.add(weight);
        weightRate.add(rate); 
        return weightRate;
    } 
}
