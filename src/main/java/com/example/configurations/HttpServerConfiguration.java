package com.example.configurations;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.handlers.AdministratorHandler;
import com.example.handlers.LoginHandler;
import com.example.handlers.MetadataHandler;
import com.example.handlers.QuizResultsHandler;
import com.example.handlers.RegistrationHandler;
import com.example.handlers.SetQuizHandler;
import com.example.handlers.SnippetHandler;
import com.example.handlers.StaticFileHandler;
import com.example.handlers.StudentPerformanceHandler;
import com.example.handlers.TakeQuizHandler;
import com.example.handlers.TeacherClasslistHandler;
import com.example.handlers.TeacherLibraryHandler;
import com.example.handlers.TeacherRosterHandler;
import com.example.handlers.TeacherSongHandler;
import com.example.handlers.ThymeleafHandler;
import com.example.utils.ApplicationUtil;
import com.sun.net.httpserver.HttpServer;

/**
 * Configuration class for HTTP server
 */
public class HttpServerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(HttpServerConfiguration.class);

    /**
     * Starts the HTTP server for the application
     *
     * @throws IOException If the server can't be started
     */
    public static void startServer() throws IOException {
        ApplicationUtil applicationConfiguration = ApplicationUtil.getInstance();
        final int PORT = applicationConfiguration.getServerPort();
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        createAPIEndpoints(server);
        server.setExecutor(null);
        server.start();
        logger.info("Server started at http://localhost:" + PORT);
    }

    /**
     * Creates all of the API endpoints used within the application.
     *
     */
    private static void createAPIEndpoints(HttpServer server) {
        server.createContext("/", new ThymeleafHandler());
        server.createContext("/static", new StaticFileHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/register", new RegistrationHandler());
        server.createContext("/api/teachersong", new TeacherSongHandler());
        server.createContext("/api/metadata", new MetadataHandler());
        server.createContext("/api/teacher/songs", new TeacherSongHandler());
        server.createContext("/api/studentperformance", new StudentPerformanceHandler());
        server.createContext("/api/setquiz", new SetQuizHandler());
        server.createContext("/api/takequiz", new TakeQuizHandler());
        server.createContext("/api/snippet", new SnippetHandler());
        server.createContext("/api/quizResults", new QuizResultsHandler());
        server.createContext("/api/administrator", new AdministratorHandler());
        server.createContext("/api/teacherClasslist", new TeacherClasslistHandler());
        server.createContext("/api/teacherRoster", new TeacherRosterHandler());
        server.createContext("/api/teacherLibrary", new TeacherLibraryHandler());

    }
}
