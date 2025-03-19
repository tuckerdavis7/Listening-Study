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
        String content = "";

        if (path.equals("/")) {
            content = renderIndexPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/bugreports")) {
            content = renderBugPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/administrator/dashboard")) {
            content = renderAdministratorDashboardPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/moderator/dashboard")) {
            content = renderModeratorDashboardPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.startsWith("/moderator/classlist")) {
            String[] routeStrings = path.split("/");
            
            if (routeStrings.length == 4) {
                content = renderModeratorClasslistViewPage();
                sendThymeleafResponse(exchange, content);
            }
        }
        else if (path.startsWith("/moderator/teacherlist")) {
            String[] routeStrings = path.split("/");
            
            if (routeStrings.length == 4) {
                content = renderModeratorTeacherlistViewPage();
                sendThymeleafResponse(exchange, content);
            }
        }
        else if (path.equals("/teacher/dashboard")) {
            content = renderTeacherDashboardPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/teacher/createPlaylist")) {
            content = renderTeacherCreatePlaylistPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/student/dashboard")) {
            content = renderStudentDashboardPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/student/setQuiz")) {
            content = renderStudentsetQuizPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.startsWith("/student/playlists")) {
            String[] routeStrings = path.split("/");
            
            if (routeStrings.length == 3) {
                content = renderStudentPlaylistsPage();
                sendThymeleafResponse(exchange, content);
            }
            else {
                String playlistId = routeStrings[3];
                // Check if valid playlist ID

                content = renderStudentPlaylistsPage(playlistId);
                sendThymeleafResponse(exchange, content);

            }
            
        }
        else if (path.equals("/student/performance")) {
            content = renderStudentPerformancePage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.startsWith("/teacher/playlists")) {
            String[] routeStrings = path.split("/");
            
            if (routeStrings.length == 3) {
                content = renderTeacherPlaylistsPage();
                sendThymeleafResponse(exchange, content);
            }
            else {
                content = renderTeacherViewPlaylistPage();
                sendThymeleafResponse(exchange, content);
            }
        }
        else if (path.startsWith("/teacher/classlist")) {
            String[] routeStrings = path.split("/");
            
            if (routeStrings.length == 3) {
                content = renderteacherClasslistPageView();
                sendThymeleafResponse(exchange, content);
            }
            else {
                content = renderteacherClasslistPage();
                sendThymeleafResponse(exchange, content);
            }
        }
        else if (path.equals("/student/quiz")) {
            content = renderQuizPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/student/quizResults")) {
            content = renderQuizResultsPage();
            sendThymeleafResponse(exchange, content);
        }
        else if (path.equals("/register")) {
            content = renderRegistrationPage();
            sendThymeleafResponse(exchange, content);
        }        
        else if (path.equals("/login")) {
            content = renderLoginPage();
            sendThymeleafResponse(exchange, content);
        }
        else {
            send404(exchange);
        }
    }

    private String renderIndexPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Listening App!");

        return render("index", context);
    }

    private String renderBugPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Bug Report page!");

        return render("bugReports", context);
    }

    private String renderAdministratorDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");

        return render("administratorDashboard", context);
    }

    private String renderModeratorDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");

        return render("moderatorDashboard", context);
    }

    private String renderModeratorClasslistViewPage() {
        Context context = new Context();
        context.setVariable("message", "Viewing a Class Roster!");

        return render("moderatorClasslistView", context);
    }

    private String renderModeratorTeacherlistViewPage() {
        Context context = new Context();
        context.setVariable("message", "Viewing a Teacher List!");

        return render("moderatorTeacherlistView", context);
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

    private String renderStudentPerformancePage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to Performance Page!");

        return render("studentPerformance", context);
    }

    private String renderTeacherPlaylistsPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Playlists Page!");

        return render("teacherLibrary", context);
    }   

    private String renderStudentPlaylistsPage(String playlistId) {
        Context context = new Context();
        context.setVariable("message", "Welcome to the View Playlist Page!");
        // Return playlist data from our backend using the playlistId

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

    private String renderQuizResultsPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Quiz Results Page!");

        return render("quizResults", context);
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

    private String renderteacherClasslistPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the teacher classlist Page!");

        return render("teacherClasslistview", context);
    }

    private String renderStudentsetQuizPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the student set Quiz Page!");

        return render("setQuiz", context);
    }
    
    private String renderTeacherCreatePlaylistPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the create playlist Page!");

        return render("createPlaylist", context);
    }
    

    private String renderteacherClasslistPageView() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the classlist roster page!");

        return render("teacherClasslist", context);
    }   
}
