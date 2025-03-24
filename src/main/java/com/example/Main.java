package com.example;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.example.configuration.DatabaseConfiguration;
import com.example.configuration.HttpServerConfiguration;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            HttpServerConfiguration.startServer();
        }
        catch (Exception e) {
            logger.error("Error while starting HTTP server: " + e.getMessage());
        }
        
        try {
            DatabaseConfiguration.connect();
        }
        catch (Exception e) {
            logger.error("Error while connecting to database: " + e.getMessage());
        }
    }
}