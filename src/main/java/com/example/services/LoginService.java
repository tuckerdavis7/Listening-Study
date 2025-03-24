package com.example.services;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;
public class LoginService extends BaseService {
    public String authenticateLogin(HttpExchange exchange) throws IOException {
        List<Map<String, Object>> parameters = super.getParameters(exchange);
        // Assuming the first map in the list contains the login credentials
        Map<String, Object> loginParams = parameters.get(0);
        
        String email = (String) loginParams.get("email");
        String formPassword = (String) loginParams.get("password");
        String responseString = "";
        
        try {
            ResultSet result = UserRepository.getUserByEmail(email);
            Map<String, Object> loginMap = new HashMap<>();
            while (result.next()) {
                String hashPassword = result.getString("password");
                boolean isMatching = BCrypt.checkpw(formPassword, hashPassword);
                if (isMatching) {
                    loginMap.put("role", result.getString("role"));
                    responseString = super.formatJSON(loginMap, "success"); // correct login
                }
                else {
                    responseString = super.formatJSON(loginMap, "failure"); // incorrect login
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}