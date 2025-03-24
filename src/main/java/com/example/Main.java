package com.example;

import com.example.configuration.DatabaseConfiguration;
import com.example.configuration.HttpServerConfiguration;

public class Main {
    public static void main(String[] args) {
        try {
            HttpServerConfiguration.startServer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            DatabaseConfiguration.connect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}