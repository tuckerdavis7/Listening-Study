package com.example.handlers;

import java.io.IOException;

import com.example.services.ModeratorService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModeratorHandler extends BaseHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(ModeratorHandler.class);
    private ModeratorService moderatorService;

    public ModeratorHandler() {
        this.moderatorService = new ModeratorService();
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
                if (exchange.getRequestURI().getPath().equals("/api/moderator/dashboard")) {
                    logger.info("at GET in ModeratorHandler");
                    response = moderatorService.getAllClasses(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                /*else if (exchange.getRequestURI().getPath().equals("/api/administrator/reports")) {
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
                break;*/
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
