package com.example.configuration;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.example.handlers.*;

public class HttpServerConfiguration {
    public static void startServer() throws IOException {
        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.getInstance();
        final int PORT = applicationConfiguration.getServerPort();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        createAPIEndpoints(server);
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + PORT);
    }

    //create all contexts for API here
    private static void createAPIEndpoints(HttpServer server) {
        server.createContext("/", new ThymeleafHandler());
        server.createContext("/static", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/metadata", new MetadataHandler());
        server.createContext("/api/users", new UserHandler());
    }
}
