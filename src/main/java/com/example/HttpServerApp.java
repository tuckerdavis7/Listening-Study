package com.example;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.example.handlers.*;

public class HttpServerApp {
    public static void main(String[] args) throws IOException {
        final int PORT = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + PORT);
    }
}
