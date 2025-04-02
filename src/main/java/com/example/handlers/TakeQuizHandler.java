package com.example.handlers;

import java.io.IOException;

import com.example.services.TakeQuizService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TakeQuizHandler extends BaseHandler implements HttpHandler {
    private TakeQuizService quizSettingsService;

    public TakeQuizHandler() {
        this.quizSettingsService = new TakeQuizService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            // case "POST":
            //     response = quizSettingsService.setQuizParameters(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            case "GET":
                response = quizSettingsService.getQuizSettings(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

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
