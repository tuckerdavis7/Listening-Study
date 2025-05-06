package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherClasslistService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to teacher classlist data.
 */
public class TeacherClasslistHandler extends BaseHandler implements HttpHandler {
    private TeacherClasslistService teacherClasslistService;

    /**
     * Class constructor to initialize service classlist file
     */
    public TeacherClasslistHandler() {
        this.teacherClasslistService = new TeacherClasslistService();
    }

     /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    public void handle(HttpExchange exchange) throws IOException {
        if (!super.isRequestAuthorized(exchange)) {
            return;
        }
        
        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                response = teacherClasslistService.getClasslist(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;          
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
