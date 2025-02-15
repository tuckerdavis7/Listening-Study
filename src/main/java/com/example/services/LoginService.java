package com.example.services;

import java.io.IOException;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService{
    public String authenticateLogin(HttpExchange exchange) throws IOException{
        Map<String, String> parameters = getParameters(exchange);
        String email = parameters.get("email");
        String password = parameters.get("password");

        System.out.println(email);
        System.out.println(password);

        return "authenticated";
    }
}