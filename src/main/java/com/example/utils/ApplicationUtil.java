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

    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }

    public String getDbURL() {
        return properties.getProperty("db.url");
    }

    public Integer getServerPort() {
        return Integer.parseInt(properties.getProperty("server.port"));
    }
}
