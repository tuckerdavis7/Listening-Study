package com.example.services;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.ReportRepository;
import com.sun.net.httpserver.HttpExchange;

public class AdministratorReportService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(AdministratorReportService.class);
    ReportRepository reportRepository = new ReportRepository();

    public String getAllReports(HttpExchange exchange) throws IOException {
        String responseString = "";
        try {
            ResultSet result = reportRepository.getAllReports();
            ArrayList<Map<String, Object>> reportList = new ArrayList<>();
            
            while (result.next()) {
                Map<String, Object> reportMap = new HashMap<>();
                reportMap.put("ID", result.getInt("ID"));
                reportMap.put("initialDate", result.getString("timeOfReport"));
                reportMap.put("username", result.getString("username"));
                reportMap.put("modifiedDate", result.getString("lastUpdatedTime"));
                reportMap.put("modifiedBy", result.getString("lastUpdatedBy"));
                reportMap.put("description", result.getString("description"));
                reportMap.put("email", result.getString("email"));
                reportMap.put("status", result.getString("status"));
                
                reportList.add(reportMap);
            }
            responseString = super.formatJSON(reportList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in getAllReports of AdministratorReportService: " + e.getMessage());
        }
        return responseString;
    }

    public String updateReport(HttpExchange exchange) throws IOException {
        String responseString = "";
        Map<String, Object> parameters = super.getParameters(exchange);

        try {
            reportRepository.updateReportStatus(parameters);
            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in updateReport of AdministratorReportService: " + e.getMessage());
        }

        return responseString;
    }
}