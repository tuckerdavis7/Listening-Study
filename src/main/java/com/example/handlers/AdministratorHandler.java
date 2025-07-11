package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.AdministratorService;

import java.io.IOException;

/**
 * Handler class for processing API requests related to administrator actions.
 */
public class AdministratorHandler extends BaseHandler implements HttpHandler {
    private AdministratorService administratorService;

    /**
     * Class constructor to initialize service file
     */
    public AdministratorHandler() {
        this.administratorService = new AdministratorService();
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
                if (exchange.getRequestURI().getPath().equals("/api/administrator/users")) {
                    response = administratorService.getAllUsers(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/administrator/reports")) {
                    response = administratorService.getAllReports(exchange);
                    super.sendResponse(exchange, response, "Regular");
                }

            case "PATCH":
                if (exchange.getRequestURI().getPath().equals("/api/administrator/users")) {
                    response = administratorService.updateDesignation(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals(("/api/administrator/reports"))) {
                    response = administratorService.updateReportStatus(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                
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
