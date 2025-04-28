package com.example.utils;

import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class CookieUtil {
    /**
     * Retrieves the value of a specific cookie from the HTTP exchange.
     *
     * @param exchange The HTTP exchange containing the request data
     * @param name The name of the cookie to retrieve
     * @return The value of the cookie if found; otherwise, null
     */
    private static String getCookie(HttpExchange exchange, String name) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (cookies == null) return null;

        for (String header : cookies) {
            for (String cookie : header.split(";")) {
                String[] parts = cookie.trim().split("=", 2);
                if (parts.length == 2 && parts[0].equals(name)) {
                    return parts[1];
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the value of the "SESSIONID" cookie from the HTTP exchange.
     *
     * @param exchange The HTTP exchange containing the request data
     * @return The session ID if present; otherwise, "not found"
     */
    public static String getCookieSessionID(HttpExchange exchange) {
        String sessionId = getCookie(exchange, "SESSIONID");
        return (sessionId != null) ? sessionId : "not found";
    }

    /**
     * Checks if the request has a valid session cookie.
     *
     * @param exchange The data from the API request
     * @return true if a valid session exists, false otherwise
     */
    public boolean hasValidSession(HttpExchange exchange) {
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
    
}



