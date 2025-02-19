package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;

import com.example.repositories.LoginRepository;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService{
    public String authenticateLogin(HttpExchange exchange) throws IOException{
        Map<String, String> parameters = getParameters(exchange);
        String email = parameters.get("email");
        String formPassword = parameters.get("password");

        String responseString = "";
        try {
            ResultSet result = LoginRepository.getUserByEmail(email);
            if (result.next()) {
                String hashPassword = result.getString("password");
                String first = result.getString("first_name");
                String last = result.getString("last_name");
                boolean isMatching = BCrypt.checkpw(formPassword, hashPassword);

                if (isMatching) { //if username and password are correct
                    responseString = "Hello " + first + " " + last;
                }
                
                else{ //correct email, wrong password
                    responseString = "Incorrect password";
                }
            }
            
            else { //incorrect email
                responseString = "Invalid email";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return responseString;
    }
        
}