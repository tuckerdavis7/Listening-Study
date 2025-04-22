package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the meta data view.
 */
public class MetadataRepository {
    /**
     * Returns the most recent meta data added
     *
     * @throws SQLException When the query does not run properly
     */
    public ResultSet getApplicationMetadata() throws SQLException {
        String query = "SELECT * FROM view_metadata ORDER BY dataID desc LIMIT 1";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
