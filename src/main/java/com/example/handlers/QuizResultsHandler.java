package com.example.handlers;

import java.io.IOException;

import com.example.services.QuizResultsService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to quiz results.
 */
public class QuizResultsHandler extends BaseHandler implements  HttpHandler {
    private QuizResultsService quizResultsService;

    /**
     * Class constructor to intialize service file
     */
    public QuizResultsHandler() {
        this.quizResultsService = new QuizResultsService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!super.isRequestAuthorized(exchange)) {
            return;
        }

        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/api/quizResults/settings")) {
                    response = quizResultsService.getQuizSettings(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/quizResults/wrongAnswers")) {
                    response = quizResultsService.getQuizResults(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                

            case "POST":
                if (exchange.getRequestURI().getPath().equals("/api/quizResults/submit")) {
                    response = quizResultsService.submitAnswer(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/quizResults/correct")) {
                    response = quizResultsService.getCorrectAnswers(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else {
                    super.sendResponse(exchange, "Method Not Allowed", "Regular");
                    break;
                }  

            // case "PATCH":
            //     response = quizSettingsService.updateDesignation(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            // case "DELETE":
            //     response = quizSettingsService.deleteUser(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
