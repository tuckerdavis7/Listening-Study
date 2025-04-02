package com.example.handlers;

import java.io.IOException;

import com.example.services.SetQuizService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
                response = setQuizService.setQuizParameters(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "GET":
                response = setQuizService.getPlaylists(exchange);
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
