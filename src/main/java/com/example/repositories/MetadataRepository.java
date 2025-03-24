package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configuration.DatabaseConfiguration;

public class MetadataRepository {
    public ResultSet getApplicationMetadata() throws SQLException {
        String query = "SELECT * FROM view_metadata ORDER BY dataID desc LIMIT 1";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
