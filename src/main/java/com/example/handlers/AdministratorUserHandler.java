package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.AdministratorUserService;

import java.io.IOException;

public class AdministratorUserHandler extends BaseHandler implements HttpHandler {
    private AdministratorUserService administratorService;

    public AdministratorUserHandler() {
        this.administratorService = new AdministratorUserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = administratorService.getAllUsers(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "PATCH":
                response = administratorService.updateDesignation(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "DELETE":
                response = administratorService.deleteUser(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
