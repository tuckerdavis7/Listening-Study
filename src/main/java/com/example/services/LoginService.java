package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;
/**
 * Service class for taking API requests, processing, and sending queries related to user login.
 */
public class LoginService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    UserRepository userRepository = new UserRepository();

    /**
     * Takes the username and password of a username and compares it to the details in the database
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String authenticateLogin(HttpExchange exchange) throws IOException {
        Map<String, Object> loginParams = super.getParameters(exchange);
       
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