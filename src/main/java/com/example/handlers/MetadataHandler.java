package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.MetadataService;

import java.io.IOException;

public class MetadataHandler extends BaseHandler implements HttpHandler {
    private MetadataService metadataService;

    public MetadataHandler() {
        this.metadataService = new MetadataService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = metadataService.getMetadata(exchange);
                sendResponse(exchange, response);
                break;
            
            default:
                sendResponse(exchange, "Method Not Allowed");
                break;
        }        
    }
}
