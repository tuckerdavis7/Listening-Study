package com.example.handlers;

import com.example.services.BaseService;
import com.example.utils.CookieUtil;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Base Handler class that contains methods to be used in other handlers.
 */
public class BaseHandler {
    private BaseService baseService;
    private CookieUtil cookieUtil;

    /**
     * Class constructor to intialize service and util files
     */
    public BaseHandler() {
        this.baseService = new BaseService();
        this.cookieUtil = new CookieUtil();
    }

    /**
     * Validates if the request has a valid session and matching role.
     *
     * @param exchange The HTTP exchange containing the request
     * @return true if the request is authorized; false otherwise
     * @throws IOException If redirection fails
     */
    protected boolean isRequestAuthorized(HttpExchange exchange) throws IOException {
        if (!cookieUtil.hasValidSession(exchange)) {
            redirectToRoot(exchange);
            return false;
        }

        String role = extractRoleFromUrl(exchange);
        boolean isRoleMatching = baseService.compareRoles(exchange, role);
        if (!isRoleMatching) {
            redirectToUnauthorized(exchange);
            return false;
        }

        return true;
    }

    /**
     * Helper to redirect the request to the application root URL.
     *
     * @param exchange The HTTP exchange to redirect
     * @throws IOException If an I/O error occurs during redirection
     */
    protected void redirectToRoot(HttpExchange exchange) throws IOException {
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
     * Helper to redirect the request to the application unauthorized page's URL.
     *
     * @param exchange The HTTP exchange to redirect
     * @throws IOException If an I/O error occurs during redirection
     */
    protected void redirectToUnauthorized(HttpExchange exchange) throws IOException {        
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        String jsonResponse = "{\"error\":\"unauthorized\",\"redirect\":\"/unauthorized\"}";
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(403, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }

        exchange.close();
    }

    /**
     * Helper to extract the role from the browser URL path.
     * For example, from "/administrator/dashboard" or "localhost:8080/administrator/dashboard" 
     * it will extract "administrator".
     *
     * @param exchange The HTTP exchange containing the request
     * @return The extracted role (student, teacher, moderator, administrator) or null if not found
     */
    protected String extractRoleFromUrl(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        
        // Split the path by "/" and check each segment
        String[] segments = path.split("/");
        
        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i].trim();
            if (!segment.isEmpty()) {
                // Check if the segment is one of our defined roles
                if (segment.equals("student") || 
                    segment.equals("teacher") || 
                    segment.equals("moderator") || 
                    segment.equals("administrator")) {
                    return segment;
                }
            }
        }        
        return null;
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
