package com.example.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.models.UserModel;
import com.sun.net.httpserver.HttpExchange;

public class UserService extends BaseService {

    public String getUsers(HttpExchange exchange) {
        String responseString = "";
        ArrayList<String> nameList = new ArrayList<String>();
        try {
            ResultSet result = UserModel.selectAllFirstAndLastNames();
            // 
            while (result.next()) {
                String name = result.getString("first_name") + " " + result.getString("last_name");
                nameList.add(name);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        for (String name : nameList) {
            responseString += name + "<br>";
        }

        return responseString;
    }
}
