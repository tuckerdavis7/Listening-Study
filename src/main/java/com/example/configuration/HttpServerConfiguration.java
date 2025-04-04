package com.example.configuration;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.handlers.*;

public class HttpServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerConfiguration.class);

    public static void startServer() throws IOException {
        ApplicationConfiguration applicationConfiguration = ApplicationConfiguration.getInstance();
        final int PORT = applicationConfiguration.getServerPort();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        createAPIEndpoints(server);
        server.setExecutor(null);
        server.start();
        logger.info("Server started at http://localhost:" + PORT);
    }

    //create all contexts for API here
    private static void createAPIEndpoints(HttpServer server) {
        server.createContext("/", new ThymeleafHandler());
        server.createContext("/static", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/register", new RegistrationHandler());
        server.createContext("/api/metadata", new MetadataHandler());
        server.createContext("/api/teacher/songs", new TeacherSongHandler());
        server.createContext("/api/studentperformance", new StudentPerformanceHandler());
        server.createContext("/api/setquiz", new SetQuizHandler());
        server.createContext("/api/takequiz", new TakeQuizHandler());
        server.createContext("/api/snippet", new SnippetHandler());
        server.createContext("/api/quizResults", new QuizResultsHandler());
    }
}
