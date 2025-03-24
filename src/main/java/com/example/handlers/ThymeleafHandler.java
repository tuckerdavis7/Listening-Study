package com.example.handlers;

import java.io.IOException;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ThymeleafHandler extends BaseHandler implements HttpHandler {
    private TemplateEngine templateEngine;

    public ThymeleafHandler() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); //check template folder
        templateResolver.setSuffix(".html"); //checks html files
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

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

    //base rendering function for all pages
    private String render(String template, Context context) {
        try {
            return templateEngine.process(template, context);
        } catch (Exception e) {
            System.err.println("Error processing Thymeleaf template: " + e.getMessage());
            e.printStackTrace();
            return "<h1>Template rendering failed</h1>";
        }
    }
}