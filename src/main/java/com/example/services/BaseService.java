package com.example.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;

public class BaseService {
    private Gson gson = new Gson();

    protected Map<String, String> getParameters(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }

        return gson.fromJson(body.toString(), new TypeToken<Map<String, String>>(){}.getType());
        }
}