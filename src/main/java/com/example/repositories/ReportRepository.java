package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the reports table.
 */
public class ReportRepository {
     /**
     * Returns all reports
     *
     * @throws SQLException When the query does not run properly
     * @return ResultSet containing query results
     */
    public ResultSet getAllReports() throws SQLException {
        String query = "SELECT * FROM reportTime";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

     /**
     * Modifies the report table by changing the status of a report
     *
     * @param report Map that that contains the status if report and id.
     * @throws SQLException When the query does not run properly    
     */
    public void updateReportStatus(Map<String, Object> report) throws SQLException {
        String query = "UPDATE reportTime SET status = ?, resolution = ? WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        Object id = report.get("id");

        pstmt.setString(1, report.get("status").toString());
        pstmt.setString(2, report.get("resolution").toString());
        pstmt.setInt(3, Integer.parseInt(id.toString()));
        pstmt.executeUpdate();
    }
}
