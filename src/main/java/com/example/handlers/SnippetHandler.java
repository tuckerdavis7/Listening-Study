package com.example.handlers;

import java.io.IOException;

import com.example.services.SnippetService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class SnippetHandler extends BaseHandler implements HttpHandler {
    private SnippetService snippetService;

    public SnippetHandler() {
        this.snippetService = new SnippetService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST":
                response = snippetService.getTimeStamp(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            // case "PATCH":
            //     response = studentPerformanceService.updateDesignation(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;

            // case "DELETE":
            //     response = studentPerformanceService.deleteUser(exchange);
            //     super.sendResponse(exchange, response, "Regular");
            //     break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }



}
