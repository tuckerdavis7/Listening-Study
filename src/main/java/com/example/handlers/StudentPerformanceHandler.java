package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.StudentPerformanceService;

import java.io.IOException;

/**
 * Handler class for processing API requests related to viewing the student performance page.
 */
public class StudentPerformanceHandler extends BaseHandler implements HttpHandler {
    private StudentPerformanceService studentPerformanceService;

    /**
     * Class constructor to initialize service file
     */
    public StudentPerformanceHandler() {
        this.studentPerformanceService = new StudentPerformanceService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!super.isRequestAuthorized(exchange)) {
            return;
        }

        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = studentPerformanceService.getSongPerformances(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
