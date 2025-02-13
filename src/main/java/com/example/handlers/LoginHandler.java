package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.LoginService;

import java.io.IOException;

public class LoginHandler extends BaseHandler implements HttpHandler {
    private LoginService loginService;

    public LoginHandler() {
        this.loginService = new LoginService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        
        if ("GET".equals(exchange.getRequestMethod())) {
            response = loginService.authenticateLogin(exchange);
        }
        
        sendResponse(exchange, response);
    }
}
