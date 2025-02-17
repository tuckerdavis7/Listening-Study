package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import com.example.models.LoginModel;
import com.sun.net.httpserver.HttpExchange;

public class LoginService extends BaseService{
    public String authenticateLogin(HttpExchange exchange) throws IOException{
        Map<String, String> parameters = getParameters(exchange);
        String email = parameters.get("email");
        String formPassword = parameters.get("password");

        String returnString = "";
        try {
            ResultSet result = LoginModel.verifyUser(email);
            if (result.next()) {
                String hashPassword = result.getString("password");
                String first = result.getString("first_name");
                String last = result.getString("last_name");
                boolean isMatching = BCrypt.checkpw(formPassword, hashPassword);
                
                //if username and password are correct
                if (isMatching) {
                    returnString = "Hello " + first + " " + last;
                }
                //correct email, wrong password
                else{
                    returnString = "Incorrect password";
                }
            }
            //incorrect email
            else {
                returnString = "Invalid email";
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return returnString;
    }
        
}