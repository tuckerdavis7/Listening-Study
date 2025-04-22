package com.example;

import org.slf4j.LoggerFactory;

import com.example.configurations.DatabaseConfiguration;
import com.example.configurations.HttpServerConfiguration;

import org.slf4j.Logger;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            HttpServerConfiguration.startServer();
        }
        catch (Exception e) {
            logger.error("Error while starting HTTP server:");
            e.printStackTrace();
        }
        
        try {
            DatabaseConfiguration.connect();
        }
        catch (Exception e) {
            logger.error("Error while connecting to database:");
            e.printStackTrace();
        }
    }
}