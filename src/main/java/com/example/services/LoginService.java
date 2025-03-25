package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    UserRepository userRepository = new UserRepository();

    public String authenticateLogin(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> parameters = super.getParameters(exchange);
        Map<String, Object> loginParams = parameters.get(0);
        
        String email = (String) loginParams.get("email");
        String formPassword = (String) loginParams.get("password");
        String responseString = "";
        
        try {
            ResultSet result = userRepository.getUserByEmail(email);
            Map<String, Object> loginMap = new HashMap<>();
            responseString = super.formatJSON(loginMap, "failure"); // incorrect login

            while (result.next()) {
                String hashPassword = result.getString("password");
                boolean isMatching = BCrypt.checkpw(formPassword, hashPassword);
                if (isMatching) {
                    loginMap.put("role", result.getString("role"));
                    responseString = super.formatJSON(loginMap, "success"); // correct login
                }
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in authenticateLogin of LoginService: " + e.getMessage());
        }
        return responseString;
    }
}