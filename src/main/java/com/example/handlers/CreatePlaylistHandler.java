package com.example.handlers;

import java.io.IOException;

import com.example.services.CreatePlaylistService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to the teacher's playlist creation page.
 */
public class CreatePlaylistHandler extends BaseHandler implements HttpHandler {
    private CreatePlaylistService createPlaylistService;

    /**
     * Class constructor to intialize service file
     */
    public CreatePlaylistHandler() {
        this.createPlaylistService = new CreatePlaylistService();
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
                response = createPlaylistService.getClassOptions(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            case "POST":
                System.out.println("POST HANDLER");
                response = createPlaylistService.createPlaylist(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
                
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
