package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

import com.example.repositories.LoginRepository;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService{
    public String authenticateLogin(HttpExchange exchange) throws IOException{
        Map<String, String> parameters = getParameters(exchange);
        String email = parameters.get("email");
        String formPassword = parameters.get("password");

        String responseString = "";
        try {
            ResultSet result = LoginRepository.getUserByEmail(email);
            Map<String, Object> loginMap = new HashMap<>();
            while (result.next()) {
                String hashPassword = result.getString("password");
                boolean isMatching = BCrypt.checkpw(formPassword, hashPassword);

                if (isMatching) {
                    responseString = formatJSON(loginMap, "success"); //correct login
                }
                else{
                    responseString = formatJSON(loginMap, "failure"); //incorrect login
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return responseString;
    }
        
}