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
     * Retrieves all reports associated with a specific user based on their user ID.
     *
     * @param userID The unique ID of the user whose reports are to be fetched.
     * @return ResultSet containing all matching reports from the reportTime table.
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getReportsByUserID(Integer userID) throws SQLException {
        String query = "SELECT * FROM reportTime where email = (SELECT email from users u where u.user_id = ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);

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
        String query = "UPDATE reportTime SET status = ?, resolution = ?, lastUpdatedBy = ?, lastUpdatedTime = NOW() WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        Object id = report.get("id");

        pstmt.setString(1, report.get("status").toString());
        pstmt.setString(2, report.get("resolution").toString());
        pstmt.setString(3, report.get("lastUpdatedBy").toString());
        pstmt.setInt(4, Integer.parseInt(id.toString()));
        pstmt.executeUpdate();
    }

    /**
     * Marks a report as resolved by updating its status and resolution fields.
     *
     * @param report Map that contains the ID of the report to be resolved.
     * @throws SQLException When the query does not run properly
     */
    public void resolveReport(Map<String, Object> report) throws SQLException {
        String query = "UPDATE reportTime SET status = ?, resolution = ? WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        Object id = report.get("id");

        pstmt.setString(1, "Resolved");
        pstmt.setString(2, "Resolved by User");
        pstmt.setInt(3, Integer.parseInt(id.toString()));
        pstmt.executeUpdate();
    }

    /**
     * Adds a new report to the reportTime table with default status as "Open" and current time as time of report.
     *
     * @param report Map that contains email, description, and lastUpdatedBy fields for the report.
     * @throws SQLException When the query does not run properly
     */
    public void addReport(Map<String, Object> report) throws SQLException {
        String query = "INSERT into reportTime (timeOfReport, email, description, resolution, lastUpdatedTime, lastUpdatedBy, status) VALUES (NOW(), ?, ?, ?, NOW(), ?, ?)";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        pstmt.setString(1, report.get("email").toString());
        pstmt.setString(2, report.get("description").toString());
        pstmt.setString(3, "");
        pstmt.setString(4, report.get("lastUpdatedBy").toString());
        pstmt.setString(5, "Open");
        pstmt.executeUpdate();
    }
}
