package com.example.handlers;

import java.io.IOException;

import com.example.services.ModeratorService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Handler class for processing API requests related to moderator data.
 */
public class ModeratorHandler extends BaseHandler implements HttpHandler {
    private ModeratorService moderatorService;

    /**
     * Class constructor to intialize moderator service file
     */
    public ModeratorHandler() {
        this.moderatorService = new ModeratorService();
    }

    /**
     * Handles/routes HTTP requests from frontend to proper service method
     *
     * @param exchange The data from the API request
     * @throws IOException If HTTP request send or recieve operations fail
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!super.isRequestAuthorized(exchange)) {
            return;
        }

        String response = "";
        String method = exchange.getRequestMethod();
        
        switch (method) {
            case "GET":
                if (exchange.getRequestURI().getPath().equals("/api/moderator/dashboard")) {
                    response = moderatorService.getAllClasses(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/teacherlist")) {
                    response = moderatorService.getClassTeachers(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/classlist")) {
                    response = moderatorService.getClassStudents(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                break;

            case "POST":
                if (exchange.getRequestURI().getPath().equals("/api/moderator/dashboard")) {
                    response = moderatorService.addClass(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/teacherlist")) {
                    response = moderatorService.addTeacher(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/classname")) {
                    response = moderatorService.changeClassname(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/classlist")) {
                    response = moderatorService.addStudent(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                break;

            case "PATCH":
                if (exchange.getRequestURI().getPath().equals("/api/moderator/teacherlist")) {
                    response = moderatorService.removeTeacher(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/classlist")) {
                    response = moderatorService.removeStudent(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                break;

            case "DELETE":
                response = moderatorService.deleteClass(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
