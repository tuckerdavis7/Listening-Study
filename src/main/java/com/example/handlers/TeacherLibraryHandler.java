package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherLibraryService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to teacher library data.
 */
public class TeacherLibraryHandler extends BaseHandler implements HttpHandler{
    private TeacherLibraryService teacherLibraryService;

    /**
     * Class constructor to intialize service library file
     */
    public TeacherLibraryHandler() {
        this.teacherLibraryService = new TeacherLibraryService();
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
