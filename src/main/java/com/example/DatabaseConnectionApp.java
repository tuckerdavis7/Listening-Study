package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//once we get another SQL query running, we can probably get rid of the select users, and just have the main method be the logic of createConncection
public class DatabaseConnectionApp {
    private static Connection con;
    public static void main(String[] args) throws IOException {
        createConnection();
        selectUsers();
        
    }

    private static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/listeningapp", "root", "root");
            
            System.out.println("Database connection is successful.");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    private static void selectUsers() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users;");

            while (rs.next()) {
                String username = rs.getString("username");
                System.out.println(username);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
