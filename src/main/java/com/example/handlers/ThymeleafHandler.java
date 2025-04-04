package com.example.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for returning dynamic thymeleaf templates and fragments.
 */
public class ThymeleafHandler extends BaseHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(ThymeleafHandler.class);
    private TemplateEngine templateEngine;

    /**
     * Class constructor to create the templating engine
     */
    public ThymeleafHandler() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); //check template folder
        templateResolver.setSuffix(".html"); //checks html files
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    /**
     * Handles/routes dynamic thymeleaf requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If template request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] routeParts = path.split("/");
        
        // Default welcome message
        String welcomeMessage = "Welcome to the Listening App!";
        String template = null;
        
        // Map paths to templates
        if (path.equals("/")) {
            template = "index";
        } else if (path.equals("/bugreports")) {
            template = "bugReports";
            welcomeMessage = "Welcome to the Bug Report page!";
        } else if (path.equals("/administrator/dashboard")) {
            template = "administratorDashboard";
            welcomeMessage = "Welcome to the Dashboard!";
        } else if (path.equals("/moderator/dashboard")) {
            template = "moderatorDashboard";
            welcomeMessage = "Welcome to the Dashboard!";
        } else if (path.startsWith("/moderator/classlist") && routeParts.length == 4) {
            template = "moderatorClasslistView";
            welcomeMessage = "Viewing a Class Roster!";
        } else if (path.startsWith("/moderator/teacherlist") && routeParts.length == 4) {
            template = "moderatorTeacherlistView";
            welcomeMessage = "Viewing a Teacher List!";
        } else if (path.equals("/teacher/dashboard")) {
            template = "teacherDashboard";
            welcomeMessage = "Welcome to the Dashboard!";
        } else if (path.equals("/teacher/createPlaylist")) {
            template = "createPlaylist";
            welcomeMessage = "Welcome to the create playlist Page!";
        } else if (path.equals("/student/dashboard")) {
            template = "studentDashboard";
            welcomeMessage = "Welcome to the Dashboard!";
        } else if (path.equals("/student/setQuiz")) {
            template = "setQuiz";
            welcomeMessage = "Welcome to the student set Quiz Page!";
        } else if (path.startsWith("/student/playlists")) {
            if (routeParts.length == 3) {
                template = "studentLibrary";
                welcomeMessage = "Welcome to the Playlists Page!";
            } else {
                template = "studentPlaylist";
                welcomeMessage = "Welcome to the View Playlist Page!";
                // Playlist ID is available in routeParts[3] if needed
            }
        } else if (path.equals("/student/performance")) {
            template = "studentPerformance";
            welcomeMessage = "Welcome to Performance Page!";
        } else if (path.startsWith("/teacher/playlists")) {
            if (routeParts.length == 3) {
                template = "teacherLibrary";
                welcomeMessage = "Welcome to the Playlists Page!";
            } else {
                template = "teacherPlaylist";
                welcomeMessage = "Welcome to the View Playlist Page!";
            }
        } else if (path.startsWith("/teacher/classlist")) {
            if (routeParts.length == 3) {
                template = "teacherClasslist";
                welcomeMessage = "Welcome to the classlist roster page!";
            } else {
                template = "teacherClasslistview";
                welcomeMessage = "Welcome to the teacher classlist Page!";
            }
        } else if (path.equals("/student/quiz")) {
            template = "quiz";
            welcomeMessage = "Welcome to the Quiz Page!";
        } else if (path.equals("/student/quizResults")) {
            template = "quizResults";
            welcomeMessage = "Welcome to the Quiz Results Page!";
        } else if (path.equals("/register")) {
            template = "registration";
            welcomeMessage = "Welcome to the Registration Page!";
        } else if (path.equals("/login")) {
            template = "login";
            welcomeMessage = "Welcome to the Login Page!";
        }
        
        if (template != null) {
            Context context = new Context();
            context.setVariable("message", welcomeMessage);
            String content = render(template, context);
            
            super.sendResponse(exchange, content, "Thymeleaf");
        } else {
            super.sendResponse(exchange, "Method Not Allowed", "Regular");
        }
    }

    /**
     * Helper function to render requested templates
     *
     * @param template The template name to be used
     * @param context The data from the API request
     * @return String The processed template, or a messsage showing failed template rendering
     */
    private String render(String template, Context context) {
        try {
            return templateEngine.process(template, context);
        } catch (Exception e) {
            logger.error("Error processing Thymeleaf template: " + e.getMessage());
            return "<h1>Template rendering failed</h1>";
        }
    }
}