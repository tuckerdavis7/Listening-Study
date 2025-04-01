package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.StudentPerformanceRepository;
import com.sun.net.httpserver.HttpExchange;

public class StudentPerformanceService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(StudentPerformanceService.class);
    StudentPerformanceRepository studentPerformanceRepository = new StudentPerformanceRepository();

    public String getSongPerformances(HttpExchange exchange) throws IOException {
        Map<String, Object> performanceParams = super.getQueryParameters(exchange);
        Object studentID = performanceParams.get("studentID");
        logger.info("Hello:" + String.valueOf(studentID));
        String responseString = "";
        try {
            ResultSet result = studentPerformanceRepository.getPerformanceByUsername(studentID);
            ArrayList<Map<String, Object>> performanceList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> performanceMap = new HashMap<>();
                performanceMap.put("ID", result.getInt("ID"));
                performanceMap.put("StudentID", result.getInt("StudentID"));
                performanceMap.put("ClassID", result.getInt("ClassID"));
                performanceMap.put("Weight", result.getDouble("Weight"));
                performanceMap.put("Score", result.getDouble("Score"));
                performanceMap.put("SongID", result.getInt("SongID"));
                performanceMap.put("TimesCorrect", result.getInt("TimesCorrect"));
                performanceMap.put("TimesQuizzed", result.getInt("TimesQuizzed"));
                performanceList.add(performanceMap);
            }
            responseString = super.formatJSON(performanceList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllReports of AdministratorReportService: " + e.getMessage());
        }
        return responseString;
    }
}