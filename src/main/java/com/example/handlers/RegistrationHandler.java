package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.RegistrationService;

import java.io.IOException;

/**
 * Handler class for processing API requests related to user registration.
 */
public class RegistrationHandler extends BaseHandler implements HttpHandler {
    private RegistrationService registrationService;

    /**
     * Class constructor to initialize service file
     */
    public RegistrationHandler() {
        this.registrationService = new RegistrationService();
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
