package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.RegistrationService;

import java.io.IOException;

public class RegistrationHandler extends BaseHandler implements HttpHandler {
    private RegistrationService registrationService;

    public RegistrationHandler() {
        this.registrationService = new RegistrationService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST":
                response = registrationService.registerUser(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
