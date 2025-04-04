package com.example.handlers;

import java.io.IOException;

import com.example.services.QuizResultsService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class QuizResultsHandler extends BaseHandler implements  HttpHandler {
    private QuizResultsService quizResultsService;

    public QuizResultsHandler() {
        this.quizResultsService = new QuizResultsService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = quizResultsService.getQuizResults(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "POST":
                if (exchange.getRequestURI().getPath().equals("/api/quizResults/submit")) {
                    response = quizResultsService.setQuizResults(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/quizResults/forward")) {
                    response = quizResultsService.forwardQuizResults(exchange);
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
