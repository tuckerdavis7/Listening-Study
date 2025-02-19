package com.example.handlers;

import java.io.IOException;

import com.example.services.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UserHandler extends BaseHandler implements HttpHandler {
    private UserService userService;

    public UserHandler() {
        this.userService = new UserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = userService.getUsers(exchange);
                sendResponse(exchange, response);
                break;
            
            case "PUT":
                //response = userService.updateUserInfo(userId, getParameters(exchange));
                sendResponse(exchange, response);
                break;
            
            case "DELETE":
                //response = userService.deleteUser(userId);
                sendResponse(exchange, response);
                break;
            
            default:
                sendResponse(exchange, "Method Not Allowed");
                break;
        }
        
        sendResponse(exchange, response);
    }
}
