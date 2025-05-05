package com.example.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for application properties
 */
public class ApplicationUtil {
    private static final String PROPERTIES_FILE = "application.properties";
    private static Properties properties = new Properties();
    private static ApplicationUtil instance = null;
    
    /**
     * Class constructor to load properties file
     */
    public ApplicationUtil() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
        } 
        catch (Exception e) {
            throw new RuntimeException("Error loading configuration properties", e);
        }
    }
    
    /**
     * Creates a retrievable singleton instance for other files
     *
     * @return A retrievable instance of property configuration
     */
    //creates retrievable singleton instance from other files
    public static ApplicationUtil getInstance() {
        if (instance == null) {
            synchronized (ApplicationUtil.class) {
                if (instance == null) {
                    instance = new ApplicationUtil();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the dbusername
     *
     * @return String containting dbusername
     */
    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    /**
     * Returns the dbpassword
     *
     * @return String containting dbpassword
     */
    public String getDbPassword() {
        return properties.getProperty("db.password");
    }

    /**
     * Returns the URL
     *
     * @return String containting dbURL
     */
    public String getDbURL() {
        return properties.getProperty("db.url");
    }

    /**
     * Returns the server port
     *
     * @return String containting the server port
     */
    public Integer getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
}
