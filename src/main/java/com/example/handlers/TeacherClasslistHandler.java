package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherClasslistService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class TeacherClasslistHandler extends BaseHandler implements HttpHandler {
    private TeacherClasslistService teacherClasslistService;

    public TeacherClasslistHandler() {
        this.teacherClasslistService = new TeacherClasslistService();
    }

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
