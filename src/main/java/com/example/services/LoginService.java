package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.SessionRepository;
import com.example.repositories.UserRepository;
import com.example.utils.CookieUtil;
import com.sun.net.httpserver.HttpExchange;
/**
 * Service class for taking API requests, processing, and sending queries related to user login.
 */
public class LoginService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    UserRepository userRepository = new UserRepository();
    SessionRepository sessionRepository = new SessionRepository();

    /**
     * Checks if a session exists and makes one if it does not.
     *
     * @param exchange The data from the API request
     * @throws IOException If data processing fails
     * @return String JSON formatted string of data for frontend
     */
    public String checkExistingSession(HttpExchange exchange) throws IOException {
        Map<String, Object> loginMap = new HashMap<>();
        String responseString = "";
        String sessionID = CookieUtil.getCookieSessionID(exchange);

        if (sessionID != null) {
            try {
                ResultSet result = sessionRepository.getUserRoleBySessionID(sessionID);
                if (result.next()) {
                    loginMap.put("role", result.getString("role"));
                }
            }
            catch (Exception e) {
                responseString = "Internal Server Error";
                logger.error("Error in checkExistingSession of LoginService: ");
                e.printStackTrace();
            }
        }
        responseString = super.formatJSON(loginMap, "success");
        return responseString;
    }

    /**
     * Takes the email and password of a user and compares it to the details in the database
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

                    //initialize session for user
                    String sessionID = UUID.randomUUID().toString();
                    sessionRepository.createSession(sessionID, result.getInt("user_id"), loginMap.get("role").toString());
                    exchange.getResponseHeaders().add("Set-Cookie", "SESSIONID=" + sessionID + "; HttpOnly; Path=/");
                }
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in authenticateLogin of LoginService: ");
            e.printStackTrace();
        }
        return responseString;
    }

    /**
     * Logs out the user by deleting their session from the database and expiring the session cookie.
     *
     * @param exchange The data from the API request
     * @return An empty response string
     */
    public String userLogout(HttpExchange exchange) {
        String response = "";
        String sessionID = CookieUtil.getCookieSessionID(exchange);

        if(sessionID != null) {
            try {
                sessionRepository.deleteSession(sessionID);
            }
            catch (SQLException e) {
                logger.error("Error in userLogout of LoginService: ");
            }
            exchange.getResponseHeaders().add("Set-Cookie", "SESSIONID=deleted; Path=/; Max-Age=0");
        }

        return response;
    }
}