package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.SetQuizService;

import java.io.IOException;

public class SetQuizHandler extends BaseHandler implements HttpHandler {
    private SetQuizService setQuizService;

    public SetQuizHandler() {
        this.setQuizService = new SetQuizService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST":
                response = setQuizService.getSongPerformances(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            // case "PATCH":
            //     response = setQuizService.updateDesignation(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            // case "DELETE":
            //     response = setQuizService.deleteUser(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
