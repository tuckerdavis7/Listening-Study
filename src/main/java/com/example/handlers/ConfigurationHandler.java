package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.ConfigurationService;

import java.io.IOException;

/**
 * Handler class for processing API requests related to metadata.
 */
public class ConfigurationHandler extends BaseHandler implements HttpHandler {
    private ConfigurationService metadataService;

    /**
     * Class constructor to intialize service file
     */
    public ConfigurationHandler() {
        this.metadataService = new ConfigurationService();
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
                response = metadataService.getMetadata(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
