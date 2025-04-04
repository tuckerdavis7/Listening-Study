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

    public TeacherSongHandler() {
        this.teacherSongService = new TeacherSongService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                response = teacherSongService.getPlaylistSongs(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "POST":
                response = teacherSongService.addSong(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
