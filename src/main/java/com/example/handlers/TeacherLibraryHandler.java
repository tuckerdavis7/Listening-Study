package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherLibraryService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


public class TeacherLibraryHandler extends BaseHandler implements HttpHandler{
    private TeacherLibraryService teacherLibraryService;

    public TeacherLibraryHandler() {
        this.teacherLibraryService = new TeacherLibraryService();
    }

    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                System.out.print("In get method");
                response = teacherLibraryService.getLibrary(exchange);
                System.out.print("Out of get method!");
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }

}
