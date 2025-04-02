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
                response = takeQuizService.getQuizSettings(exchange);
            super.sendResponse(exchange, response, "Regular");
                break;

            // case "PATCH":
            //     response = takeQuizService.updateDesignation(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            // case "DELETE":
            //     response = takeQuizService.deleteUser(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
