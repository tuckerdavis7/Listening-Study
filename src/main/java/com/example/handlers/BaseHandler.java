package com.example.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHandler {
    protected void sendResponse(HttpExchange exchange, String responseContent, String responseType) throws IOException {
        int responseCode;
        switch (responseContent) {
            case "Not Found":
                responseCode = 404;
                break;

            case "Method Not Allowed":
                responseCode = 405;
                break;

            case "Internal Server Error":
                responseCode = 500;
                break;
        
            default:
                responseCode = 200;
                break;
        }

        if (responseType != "Thymeleaf") {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
        }
        
        byte[] responseBytes = responseContent.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(responseCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
