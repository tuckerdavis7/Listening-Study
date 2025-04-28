package com.example.handlers;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.services.ModeratorService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ModeratorHandler extends BaseHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(ModeratorHandler.class);
    private ModeratorService moderatorService;

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
                    logger.info("at GET1 in ModeratorHandler");
                    response = moderatorService.getAllClasses(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/teacherlist")) {
                    logger.info("at GET2 in ModeratorHandler");
                    response = moderatorService.getClassTeachers(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                else if (exchange.getRequestURI().getPath().equals("/api/moderator/classlist")) {
                    logger.info("at GET3 in ModeratorHandler");
                    response = moderatorService.getClassStudents(exchange);
                    super.sendResponse(exchange, response, "Regular");
                    break;
                }
                break;

            case "POST":
                if (exchange.getRequestURI().getPath().equals("/api/moderator/dashboard")) {
                    logger.info("at POST1 in ModeratorHandler");
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
                logger.info("at DELETE in ModeratorHandler");
                response = moderatorService.deleteClass(exchange);
                super.sendResponse(exchange, response, "Regular");
                break;
            
            default:
                super.sendResponse(exchange, "Method Not Allowed", "Regular");
                break;
        }        
    }
}
