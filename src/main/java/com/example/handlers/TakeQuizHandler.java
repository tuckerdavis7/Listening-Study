package com.example.handlers;

import java.io.IOException;

import com.example.services.TakeQuizService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TakeQuizHandler extends BaseHandler implements HttpHandler {
    private TakeQuizService takeQuizService;

    public TakeQuizHandler() {
        this.takeQuizService = new TakeQuizService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            // case "POST":
            //     response = takeQuizService.setQuizParameters(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            case "GET":
                if (exchange.getRequestURI().getPath().equals("/api/takequiz/settings")) {
                    response = takeQuizService.getQuizSettings(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                } 
                else if (exchange.getRequestURI().getPath().equals("/api/takequiz/songs")) {
                    response = takeQuizService.getSongs(exchange);
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
