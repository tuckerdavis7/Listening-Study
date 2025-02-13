package com.example.services;

import com.sun.net.httpserver.HttpExchange;

public class LoginService {

    public String authenticateLogin(HttpExchange exchange) {
        System.out.println(exchange);
        return "authenticated";
    }
}