package com.example;

public class Main {
    public static void main(String[] args) {
        try {
            HttpServerApp.main(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        try {
            DatabaseConnectionApp.main(args);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}