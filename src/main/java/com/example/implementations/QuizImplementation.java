package com.example.implementations;

import java.util.Map;

/**
 * Implementation class for extra processing of quizzes.
 */
public class QuizImplementation {
     /**
     * Checks the answers to see if the student got the question correct
     *
     * @param quizData Map student inputted data for question
     * @param songData map is actually answer of song question
     * @return true or false if the answer matches the question
     */
    public boolean checkAnswers(Map<String, Object> quizData, Map<String, Object> songData) {
        if (((String)quizData.get("name")).toLowerCase().equals(((String)songData.get("name")).toLowerCase()) &&
            ((String)quizData.get("composer")).toLowerCase().equals(((String)songData.get("composer")).toLowerCase()) &&
            ((String)quizData.get("year")).toLowerCase().equals(((String)songData.get("year")).toLowerCase()))
            return true;
        return false;
    } 

}
