package com.example;

public class Main {
    public static void main(String[] args) {
        try {
            HttpServerApp.startServer();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            DatabaseConnectionApp.connect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}