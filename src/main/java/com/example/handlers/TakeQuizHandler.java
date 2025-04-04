package com.example.handlers;

import java.io.IOException;

import com.example.services.TakeQuizService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to the quiz page.
 */
public class TakeQuizHandler extends BaseHandler implements HttpHandler {
    private TakeQuizService takeQuizService;

    /**
     * Class constructor to intialize service file
     */
    public TakeQuizHandler() {
        this.takeQuizService = new TakeQuizService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
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
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
