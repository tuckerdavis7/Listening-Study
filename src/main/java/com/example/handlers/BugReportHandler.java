package com.example.handlers;

import java.io.IOException;

import com.example.services.BugReportService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to the bug reports page.
 */
public class BugReportHandler extends BaseHandler implements HttpHandler{
    private BugReportService bugReportService;

    /**
     * Class constructor to intialize service file
     */
    public BugReportHandler() {
        this.bugReportService = new BugReportService();
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
                response = bugReportService.getUserReports(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            case "PATCH":
                response = bugReportService.resolveReport(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "POST":
                response = bugReportService.addReport(exchange);
                super.sendResponse(exchange, response, "Regular");;
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
