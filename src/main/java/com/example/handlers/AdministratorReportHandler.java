package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.services.AdministratorReportService;

import java.io.IOException;

public class AdministratorReportHandler extends BaseHandler implements HttpHandler {
    private AdministratorReportService administratorReportService;

    public AdministratorReportHandler() {
        this.administratorReportService = new AdministratorReportService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "GET":
                response = administratorReportService.getAllReports(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            case "PATCH":
                response = administratorReportService.updateReport(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
