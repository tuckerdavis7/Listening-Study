package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handler class for serving static files back to the frontend.
 */
public class StaticFileHandler extends BaseHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticFileHandler.class);
    private final Map<String, String> contentTypeMap;
    
    /**
     * Class constructor to intialize a map containing acceptable content types
     */
    public StaticFileHandler() {
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
    
    /**
     * Handles/routes static requests from frontend to proper service method
     *
     * @param exchange The data from the static request
     * @throws IOException If static request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        if (path.startsWith("/static")) {
            String resourcePath = "static" + path.substring("/static".length());
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream != null) {
                    try {
                        setContentType(exchange, path);
                        exchange.sendResponseHeaders(200, 0); // Use chunked transfer
                        try (OutputStream os = exchange.getResponseBody()) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                os.write(buffer, 0, bytesRead);
                            }
                        }
                        return;
                    } catch (IOException e) {
                        logger.warn("Issue sending static file");
                    }
                }
            } catch (IOException e) {
                logger.error("Error opening static file:");
                e.printStackTrace();
            }
        }
        
        super.sendResponse(exchange, "Not Found", "Regular");
    }
    
    /**
     * Sets the content type of the file to be returned to the frontend.
     *
     * @param exchange The data from the static request
     * @param path The path used for gathering the file extension
     */
    private void setContentType(HttpExchange exchange, String path) {
        String extension = getFileExtension(path);
        
        // Set the content type header based on the file extension
        String contentType = contentTypeMap.getOrDefault(extension, "application/octet-stream");
        exchange.getResponseHeaders().set("Content-Type", contentType);
    }
    
    /**
     * Returns the file extension type based on the path provided
     *
     * @param path The path used for gathering the file extension
     * @return String containing the file extension
     */
    private String getFileExtension(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex != -1 && lastDotIndex < path.length() - 1) {
            return path.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }
}