package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherRosterService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to the song page from teacher view.
 */
public class TeacherRosterHandler extends BaseHandler implements HttpHandler {
    private TeacherRosterService teacherRosterService;

    /**
     * Class constructor to intialize service file
     */
    public TeacherRosterHandler() {
        this.teacherRosterService = new TeacherRosterService();
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
            case "GET":
                response = teacherRosterService.getClassRoster(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}

