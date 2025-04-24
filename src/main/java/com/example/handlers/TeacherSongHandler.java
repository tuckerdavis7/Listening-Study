package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherSongService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to the song page from teacher view.
 */
public class TeacherSongHandler extends BaseHandler implements HttpHandler {
    private TeacherSongService teacherSongService;

    /**
     * Class constructor to intialize service file
     */
    public TeacherSongHandler() {
        this.teacherSongService = new TeacherSongService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                response = teacherSongService.getPlaylistSongs(exchange);
                if (response.equals("Unauthorized"))
                    redirectToUnauthorized(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            case "POST":
                response = teacherSongService.addSong(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            case "PATCH":
                response = teacherSongService.editSong(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            case "DELETE":
                response = teacherSongService.deleteSong(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
