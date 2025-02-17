package com.example.services;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.example.DatabaseConnectionApp;
import com.example.models.LoginModel;
import java.sql.Connection;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService{
    public String authenticateLogin(HttpExchange exchange) throws IOException{
        Map<String, String> parameters = getParameters(exchange);
        String email = parameters.get("email");
        String password = parameters.get("password");

        System.out.println(email);
        System.out.println(password);

        String result = "";
        try {
            boolean userExists = LoginModel.verifyUser(email, password);
            if (userExists) {

                result = "true";
                hashPasswords();
            }
            else {
                result = "User with given email not exist";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
        public static void hashPasswords() {
        String defaultPassword = "123";
        String hashedPassword = BCrypt.hashpw(defaultPassword, BCrypt.gensalt(12));

        // SQL queries
        String selectQuery = "SELECT user_id FROM users"; // Fetch all user IDs
        String updateQuery = "UPDATE users SET password = ? WHERE user_id = ?"; // Update password

        try (Connection con = DatabaseConnectionApp.getConnection();
             PreparedStatement selectStmt = con.prepareStatement(selectQuery);
             PreparedStatement updateStmt = con.prepareStatement(updateQuery);
             ResultSet rs = selectStmt.executeQuery()) {

            int updatedCount = 0;
            while (rs.next()) {
                int userId = rs.getInt("user_id"); // Get user ID

                // Update password with hashed version of "123"
                updateStmt.setString(1, hashedPassword);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
                updatedCount++;
            }

            System.out.println("Successfully updated passwords for " + updatedCount + " users.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}