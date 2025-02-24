package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.DatabaseConnectionApp;

public class MetadataRepository {
    public static ResultSet getApplicationMetadata() throws SQLException {
        String query = "SELECT * FROM view_metadata ORDER BY dataID desc LIMIT 1";
        PreparedStatement pstmt = DatabaseConnectionApp.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
