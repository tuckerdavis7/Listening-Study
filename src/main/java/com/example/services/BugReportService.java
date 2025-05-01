package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ReportRepository;
import com.example.repositories.UserRepository;
import com.sun.net.httpserver.HttpExchange;

/**
 * Bug Repors Service class that contains methods to be used for the bug reports functionality.
 */
public class BugReportService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BugReportService.class);
    ReportRepository reportRepository = new ReportRepository();
    UserRepository userRepository = new UserRepository();

    /**
     * Retrieves all reports associated with the currently logged-in user.
     *
     * @param exchange HttpExchange object containing the session information.
     * @return A JSON-formatted string containing the user's reports or an error message.
     * @throws IOException If there is an issue reading from the exchange or writing the response.
     */
    public String getUserReports(HttpExchange exchange) throws IOException {
        String responseString = "";
        Integer userID = super.getSessionUserID(exchange);

        try {
            ResultSet result = reportRepository.getReportsByUserID(userID);
            ArrayList<Map<String, Object>> reportList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> reportMap = new HashMap<>();
                reportMap.put("ID", result.getInt("ID"));
                reportMap.put("initialDate", result.getString("timeOfReport"));
                reportMap.put("modifiedDate", result.getString("lastUpdatedTime"));
                reportMap.put("modifiedBy", result.getString("lastUpdatedBy"));
                reportMap.put("description", result.getString("description"));
                reportMap.put("email", result.getString("email"));
                reportMap.put("status", result.getString("status"));
                reportMap.put("resolution", result.getString("resolution"));
                
                reportList.add(reportMap);
            }
            responseString = super.formatJSON(reportList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllReports of AdministratorService:");
            e.printStackTrace();
        }
        return responseString;
    }

    /**
     * Resolves a report based on the parameters received from the HTTP exchange.
     *
     * @param exchange HttpExchange object containing the report ID to resolve.
     * @return A JSON-formatted success message or an error message in case of failure.
     * @throws IOException If there is an issue reading from the exchange or writing the response.
     */
    public String resolveReport(HttpExchange exchange) throws IOException {
        String responseString  = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            reportRepository.resolveReport(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in resolveReport of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

    /**
     * Adds a new report to the system using parameters from the HTTP exchange and session data.
     *
     * @param exchange HttpExchange object containing report details.
     * @return A JSON-formatted success message or an error message in case of failure.
     * @throws IOException If there is an issue reading from the exchange or writing the response.
     */
    public String addReport(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);
        Integer userID = super.getSessionUserID(exchange);

        try {
            ResultSet result = userRepository.getUserById(userID);
            if (result.next()) {
                parameters.put("email", result.getString("email").toString());
                parameters.put("lastUpdatedBy", result.getString("email").toString());
            }
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addReport of AdministratorService:");
            e.printStackTrace();
            return responseString;
        }

        try {
            reportRepository.addReport(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addReport (2) of AdministratorService:");
            e.printStackTrace();
        }

        return responseString;
    }

}
