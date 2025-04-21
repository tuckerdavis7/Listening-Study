package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.example.configuration.SessionConfiguration;
import com.example.services.LoginService;

import java.io.IOException;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.devtools.v114.audits.model.CookieIssueDetails;

import com.example.repositories.SessionRepository;
import com.example.repositories.UserRepository;

import com.example.utils.CookieUtils;

/**
 * Handler class for processing API requests related to user login.
 */
public class LoginHandler extends BaseHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    private LoginService loginService;
    SessionRepository sessionRepository = new SessionRepository();

    /**
     * Class constructor to intialize service file
     */
    public LoginHandler() {
        this.loginService = new LoginService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        String method = exchange.getRequestMethod();

        switch (method) {
            case "POST":
                if (exchange.getRequestURI().getPath().equals("/api/login/verify")) {
                    response = loginService.authenticateLogin(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                } 
                else if (exchange.getRequestURI().getPath().equals("/api/login/logout")) {
                    String sessionID = CookieUtils.getCookieSessionID(exchange);
                    if(sessionID != null) {
                        try {
                            sessionRepository.deleteSession(sessionID);
                        } catch (SQLException ex) {
                            logger.error("Error in session cookie of LoginHandler: ");
                        }
                        exchange.getResponseHeaders().add("Set-Cookie", "SESSIONID=deleted; Path=/; Max-Age=0");
                    }
                    super.sendResponse(exchange, "", "Regular");
                    break;
                }
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
