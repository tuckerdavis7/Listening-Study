package com.example.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizImplementation {
    public boolean checkAnswers(Map<String, Object> quizData, Map<String, Object> songData){
        return !(quizData.get("name") != songData.get("name") ||quizData.get("composer") != songData.get("composer") || quizData.get("year") != songData.get("year"));
    } 

    public List<Double> calculateWeight(int timesQuizzed, int timesCorrect){
        double weight = 1 - (timesCorrect/timesQuizzed);
        double rate = timesCorrect/timesQuizzed * 100;
        List<Double> weightRate = new ArrayList<>();
        weightRate.add(weight);
        weightRate.add(rate); 
        return weightRate;
    } 
}
