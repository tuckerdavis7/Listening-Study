package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.LoginService;

import java.io.IOException;

/**
 * Handler class for processing API requests related to user login.
 */
public class LoginHandler extends BaseHandler implements HttpHandler {
    private LoginService loginService;

    /**
     * Class constructor to intialize service file
     */
    public LoginHandler() {
        this.loginService = new LoginService();
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
                if (exchange.getRequestURI().getPath().equals("/api/login/verify")) {
                    response = loginService.authenticateLogin(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                } 
                else if (exchange.getRequestURI().getPath().equals("/api/login/logout")) {
                    response = loginService.userLogout(exchange);
                    super.sendResponse(exchange, "", "Regular");
                    break;
                }
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
