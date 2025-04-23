package com.example.utils;

import java.util.List;

import com.sun.net.httpserver.HttpExchange;

public class CookieUtil {
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

    public static String getCookieSessionID(HttpExchange exchange) {
        return getCookie(exchange, "SESSIONID");
    }
    
}



