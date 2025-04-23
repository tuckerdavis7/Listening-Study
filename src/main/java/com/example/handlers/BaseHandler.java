package com.example.handlers;

import com.example.services.AdministratorService;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base Handler class that contains methods to be used in other handlers.
 */
public class BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorService.class);

    /**
     * Checks if the request has a valid session cookie.
     *
     * @param exchange The data from the API request
     * @return true if a valid session exists, false otherwise
     */
    protected boolean hasValidSession(HttpExchange exchange) {
        // Get cookies from request headers
        String cookies = exchange.getRequestHeaders().getFirst("Cookie");
        
        // Check if cookies exist
        if (cookies == null || cookies.isEmpty()) {
            return false;
        }
        
        // Look for SESSIONID
        for (String cookie : cookies.split(";")) {
            String trimmedCookie = cookie.trim();
            if (trimmedCookie.startsWith("SESSIONID=")) {
                String sessionId = trimmedCookie.substring("SESSIONID=".length());
                return sessionId != null && !sessionId.isEmpty();
            }
        }
        
        return false;
    }

    /**
     * Redirects the request to the application root URL.
     *
     * @param exchange The HTTP exchange to redirect
     * @throws IOException If an I/O error occurs during redirection
     */
    protected void redirectToRoot(HttpExchange exchange) throws IOException {
        logger.warn("URL access attempt without valid session");
        exchange.getResponseHeaders().add("Set-Cookie", "SESSIONID=expired; Path=/; Max-Age=0");
        
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        String jsonResponse = "{\"error\":\"session_expired\",\"redirect\":\"/\"}";
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(401, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }

        exchange.close();
    }
    
    /**
     * Handles sending the response to the frontend.
     *
     * @param exchange The data from the API request
     * @param responseContent Parameter used to determine the HTTP code to be returned
     * @param responseType Checks if the request is static ("Thymeleaf") or dynamic (all other API calls).
     * @throws IOException If HTTP request send or recieve operations fail
     */
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
