package com.example.handlers;

import java.io.IOException;

import com.example.services.TeacherSongService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
                System.out.println("Hellodjlkfjdskf");
                response = teacherSongService.addSong(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;

            case "PATCH":
                //response = teacherSongService.deleteUser(exchange);
                //super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
