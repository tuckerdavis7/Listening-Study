package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configuration.DatabaseConfiguration;

public class ReportRepository {
    public ResultSet getAllReports() throws SQLException {
        String query = "SELECT * FROM reportTime";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

    public void updateReportStatus(Map<String, Object> user) throws SQLException {
        String query = "UPDATE report SET status = ? WHERE ID = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

        Object id = user.get("ID");

        pstmt.setString(1, user.get("status").toString());
        pstmt.setInt(2, (Integer) id);
        pstmt.executeUpdate();
    }
}
