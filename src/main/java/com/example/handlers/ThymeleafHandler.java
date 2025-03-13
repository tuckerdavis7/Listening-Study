package com.example.handlers;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.OutputStream;

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

            case "/dashboard":
                String dashboardRenderedContent = renderDashboardPage();
                sendThymeleafResponse(exchange, dashboardRenderedContent);
                break;

            case "/player":
            String playerRenderedContent = renderPlayerPage();
            sendThymeleafResponse(exchange, playerRenderedContent);
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

    private String renderDashboardPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Dashboard!");
        // context.setVariable("user", "John Doe");

        return render("dashboard", context);
    }

    private String renderPlayerPage() {
        Context context = new Context();
        context.setVariable("message", "Welcome to the Youtube Player!");
        // context.setVariable("user", "John Doe");

        return render("youtubePlayer", context);
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
