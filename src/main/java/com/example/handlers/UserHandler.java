package com.example.handlers;

import java.io.IOException;

import com.example.services.UserService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class UserHandler extends BaseHandler implements HttpHandler  {
    private UserService userService;

    public UserHandler() {
        this.userService = new UserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] splitPath = path.split("/"); // ["", "api", "users", "#"]

        if (splitPath.length == 4) {
            int userId;
            try {
                userId = Integer.parseInt(splitPath[3]);
            }
            catch (NumberFormatException e) {
                sendResponse(exchange, "Invalid user ID.", "Regular");
                return;
            }
            switch (method) {
                case "GET":
                    response = userService.getUserInfo(userId);
                    sendResponse(exchange, response, "Regular");
                    break;
                
                default:
                    sendResponse(exchange, "Method Not Allowed", "Regular");
                    break;
            }
        }
        else {
            switch (method) {
                case "GET":
                    response = userService.getAllUsers();
                    sendResponse(exchange, response, "Regular");
            }
        }
    }
    
}
