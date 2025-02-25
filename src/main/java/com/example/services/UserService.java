package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.repositories.UserRepository;

public class UserService extends BaseService {

    public String getUserInfo(int userId) throws IOException {
        String responseString = "";
        try {
            ResultSet result = UserRepository.getUserInfo(userId);
            while (result.next()) {
                Map<String, Object> userMap = new HashMap<>();

                userMap.put("user_id", result.getInt("user_id"));
                userMap.put("username", result.getString("username"));
                userMap.put("email", result.getString("email"));
                userMap.put("first_name", result.getString("first_name"));
                userMap.put("last_name", result.getString("last_name"));
                userMap.put("role", result.getString("role"));

                responseString = formatJSON(userMap, "success");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    public String getAllUsers() throws IOException {
        String responseString = "";
        try {
            ResultSet result = UserRepository.getAllUsers();
            ArrayList<Map<String, Object>> userList = new ArrayList<>();
            while (result.next()) {
                Map<String, Object> userMap = new HashMap<>();

                userMap.put("user_id", result.getInt("user_id"));
                userMap.put("username", result.getString("username"));
                userMap.put("email", result.getString("email"));
                userMap.put("first_name", result.getString("first_name"));
                userMap.put("last_name", result.getString("last_name"));
                userMap.put("role", result.getString("role"));
                userList.add(userMap);
            }
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("users", userList);
            responseString = formatJSON(responseMap, "success");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return responseString;
    }
    
}
