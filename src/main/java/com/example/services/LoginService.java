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

        //Do SQL query here (like in DatabaseConnectionApp.Java selectuUsers())
        //return the count of the query (should be 0 or 1)
        //now go to front end
        return "authenticated";

    }
        //write a function to handle de-hashing the password

}