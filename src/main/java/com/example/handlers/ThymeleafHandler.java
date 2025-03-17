package com.example.handlers;

import java.io.IOException;
import java.io.OutputStream;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ThymeleafHandler implements HttpHandler {
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

        switch (path) {
            case "/":
                String indexRenderedContent = renderIndexPage();
                sendThymeleafResponse(exchange, indexRenderedContent);
                break;

            case "/dashboard/administrator":
                String administratorDashboardRenderedContent = renderAdministratorDashboardPage();
                sendThymeleafResponse(exchange, administratorDashboardRenderedContent);
                break;

            // case "/dashboard/moderator":
            //     String moderatorDashboardRenderedContent = renderModeratorDashboardPage();
            //     sendThymeleafResponse(exchange, moderatorDashboardRenderedContent);
            //     break;

            case "/dashboard/teacher":
                String teacherDashboardRenderedContent = renderTeacherDashboardPage();
                sendThymeleafResponse(exchange, teacherDashboardRenderedContent);
                break;

            case "/dashboard/student":
                String studentDashboardRenderedContent = renderStudentDashboardPage();
                sendThymeleafResponse(exchange, studentDashboardRenderedContent);
                break;
            
            case "/student/playlists":
                String studentPlaylistsRenderedContent = renderStudentPlaylistsPage();
                sendThymeleafResponse(exchange, studentPlaylistsRenderedContent);
                break;

            case "/teacher/playlists":
                String teacherPlaylistsRenderedContent = renderTeacherPlaylistsPage();
                sendThymeleafResponse(exchange, teacherPlaylistsRenderedContent);
                break;
            
            case "/student/playlists/0001":
                String studentViewPlaylistRenderedContent = renderStudentViewPlaylistPage();
                sendThymeleafResponse(exchange, studentViewPlaylistRenderedContent);
                break;

            case "/teacher/playlists/0001":
                String teacherViewPlaylistRenderedContent = renderTeacherViewPlaylistPage();
                sendThymeleafResponse(exchange, teacherViewPlaylistRenderedContent);
                break;
            
            case "/student/quiz":
                String quizRenderedContent = renderQuizPage();
                sendThymeleafResponse(exchange, quizRenderedContent);
                break;

            case "/player":
                String playerRenderedContent = renderPlayerPage();
                sendThymeleafResponse(exchange, playerRenderedContent);
                break;

            case "/register":
                String registerRenderedContent = renderRegistrationPage();
                sendThymeleafResponse(exchange, registerRenderedContent);
                break;

            case "/login":
            String loginRenderedContent = renderLoginPage();
            sendThymeleafResponse(exchange, loginRenderedContent);
            break;

            default:
                send404(exchange);
                break;
        }
    }

    private String renderIndexPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Listening App!");

        return render("index", context);
    }

    private String renderAdministratorDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");

        return render("administratorDashboard", context);
    }

    private String renderTeacherDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");

        return render("teacherDashboard", context);
    }

    private String renderStudentDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");

        return render("studentDashboard", context);
    }

    private String renderStudentPlaylistsPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Playlists Page!");

        return render("studentLibrary", context);
    }

    private String renderTeacherPlaylistsPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Playlists Page!");

        return render("teacherLibrary", context);
    }

    private String renderStudentViewPlaylistPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the View Playlist Page!");

        return render("studentPlaylist", context);
    }

    private String renderTeacherViewPlaylistPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the View Playlist Page!");

        return render("teacherPlaylist", context);
    }

    private String renderQuizPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Quiz Page!");

        return render("quiz", context);
    }

    private String renderPlayerPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Youtube Player!");

        return render("youtubePlayer", context);
    }

    private String renderRegistrationPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Registration Page!");

        return render("registration", context);
    }

    private String renderLoginPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Login Page!");

        return render("login", context);
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

    private void sendThymeleafResponse(HttpExchange exchange, String content) throws IOException {
        byte[] response = content.getBytes();
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private void send404(HttpExchange exchange) throws IOException {
        String response = "404 Not Found";
        exchange.sendResponseHeaders(404, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
