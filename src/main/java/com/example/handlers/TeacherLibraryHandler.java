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
        if (!super.isRequestAuthorized(exchange)) {
            return;
        }

        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                response = teacherLibraryService.getLibrary(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "POST":
                response = teacherLibraryService.renamePlaylist(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }

}
