package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class StaticFileHandler extends BaseHandler implements HttpHandler {
    
    private final Map<String, String> contentTypeMap;
    
    public StaticFileHandler() {
        // Initialize content type mappings
        contentTypeMap = new HashMap<>();
        contentTypeMap.put("ico", "image/x-icon");
        contentTypeMap.put("html", "text/html");
        contentTypeMap.put("css", "text/css");
        contentTypeMap.put("js", "application/javascript");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("svg", "image/svg+xml");
        contentTypeMap.put("gif", "image/gif");
        contentTypeMap.put("json", "application/json");
        contentTypeMap.put("pdf", "application/pdf");
        contentTypeMap.put("txt", "text/plain");
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        if (path.startsWith("/static")) {
            String resourcePath = "static" + path.substring("/static".length());
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
            
            if (inputStream != null) {
                try {
                    setContentType(exchange, path);
                    byte[] fileBytes = inputStream.readAllBytes();
                    
                    // Send success response with content
                    exchange.sendResponseHeaders(200, fileBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(fileBytes);
                    }
                } finally {
                    inputStream.close();
                }
            } else {
                send404(exchange);
            }
        } else {
            send404(exchange);
        }
    }
    
    private void setContentType(HttpExchange exchange, String path) {
        String extension = getFileExtension(path);
        
        // Set the content type header based on the file extension
        String contentType = contentTypeMap.getOrDefault(extension, "application/octet-stream");
        exchange.getResponseHeaders().set("Content-Type", contentType);
    }
    
    private String getFileExtension(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < path.length() - 1) {
            return path.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
    
    private void send404(HttpExchange exchange) throws IOException {
        String response = "404 Not Found";
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(404, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}