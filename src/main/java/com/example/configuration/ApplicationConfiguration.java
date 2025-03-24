package com.example.configuration;

import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfiguration {
    private static final String PROPERTIES_FILE = "application.properties";
    private static Properties properties = new Properties();
    private static ApplicationConfiguration instance = null;
    
    private ApplicationConfiguration() {
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
    
    //creates retrievable singleton instance from other files
    public static ApplicationConfiguration getInstance() {
        if (instance == null) {
            synchronized (ApplicationConfiguration.class) {
                if (instance == null) {
                    instance = new ApplicationConfiguration();
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
