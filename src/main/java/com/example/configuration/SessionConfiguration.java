package com.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class to manage user session information across the application.
 * Stores user details such as user ID, email, and other user-specific IDs.
 */
public class SessionConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SessionConfiguration.class);
    private static SessionConfiguration instance;
   
    private Integer userId;
    private String email;
    private String role;
    private boolean isAuthenticated;
   
    /**
     * Private constructor for the singleton
     *
     */
    private SessionConfiguration() {
        this.isAuthenticated = false;
    }
   
    /**
     * Gets the singleton instance of the Session class.
     *
     * @return The Session instance
     */
    public static synchronized SessionConfiguration getInstance() {
        if (instance == null) {
            instance = new SessionConfiguration();
        }
        return instance;
    }
   
    /**
     * Initializes a session with user information.
     *
     * @param userId User's ID
     * @param email User's email
     * @param role User's role
     */
    public void initialize(Integer userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.isAuthenticated = true;
        logger.info("Session initialized for user: {}", email);
    }
   
    /**
     * Clears all session data (logs the user out).
     */
    public void logout() {
        this.userId = null;
        this.email = null;
        this.role = null;
        this.isAuthenticated = false;
        logger.info("Session logout successful.");
    }
   
    /**
     * Checks if the user is authenticated.
     *
     * @return true if user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
   
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
   
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
   
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}