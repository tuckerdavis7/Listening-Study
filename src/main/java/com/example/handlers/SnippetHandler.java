package com.example.handlers;

import java.io.IOException;

import com.example.services.SnippetService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to playing a song snippet.
 */
public class SnippetHandler extends BaseHandler implements HttpHandler {
    private SnippetService snippetService;

    /**
     * Class constructor to initialize service file
     */
    public SnippetHandler() {
        this.snippetService = new SnippetService();
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
                response = snippetService.getTimeStamp(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }



}
