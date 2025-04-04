package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Service class for taking API requests, processing, and sending queries related to user registration.
 */
public class RegistrationService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    UserRepository userRepository = new UserRepository();

    /**
     * Sends the data for a user to be registered to the database
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String registerUser(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> registrationParams = super.getParameters(exchange);

        //check if email already exists
        String email = (String) registrationParams.get("email");
        try {
            ResultSet emailResult = userRepository.getUserCountByEmail(email);
            if  (emailResult.next()) {
                int count = emailResult.getInt(1);
                if (count > 0) {
                    responseString = super.formatJSON("failure", "Account with username already exists");
                    return responseString;
                }
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in registerUser (1) of RegistrationService: " + e.getMessage());
        }

        //check if username already exists
        String username = (String) registrationParams.get("username");
        try {
            ResultSet usernameResult = userRepository.getUserCountByUsername(username);
            if  (usernameResult.next()) {
                int count = usernameResult.getInt(1);
                if (count > 0) {
                    responseString = super.formatJSON("failure", "Account with username already exists");
                    return responseString;
                }
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in registerUser (2) of RegistrationService: " + e.getMessage());
        }
        
        //add user
        String password = (String) registrationParams.get("password");
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        registrationParams.put("password", encryptedPassword);

        try {
            userRepository.addUser(registrationParams);
            responseString = super.formatJSON("success"); // correct login
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in registerUser (3) of RegistrationService: " + e.getMessage());
        }

        return responseString;
    }
}