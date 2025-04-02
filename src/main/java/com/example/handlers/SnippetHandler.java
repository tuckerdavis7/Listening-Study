package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.SnippetService;

import java.io.IOException;

public class SnippetHandler extends BaseHandler implements HttpHandler {
    private SnippetService snippetService;

    public SnippetHandler() {
        this.SnippetService = new SnippetService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = snippetServiceService.getSnippet(exchange);
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
