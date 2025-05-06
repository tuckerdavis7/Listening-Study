package com.example.configurations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.utils.ApplicationUtil;

/**
 * Configuration class for database setup
 */
public class DatabaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);
    private static Connection con;
    static ApplicationUtil applicationConfiguration = ApplicationUtil.getInstance();

    /**
     * Creates a connection to the database
     *
     * @throws ClassNotFoundException If the database driver can't be found
     * @throws Exception If the connection to the database fails
     */
    public static void connect() throws Exception{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String username = applicationConfiguration.getDbUsername();
            String password = applicationConfiguration.getDbPassword();
            String url = applicationConfiguration.getDbURL();

            con = DriverManager.getConnection(url, username, password);
            logger.info("Database connection is successful");            
        }
        catch (ClassNotFoundException e) {
            logger.error("Database driver class not found:" + e.getMessage());
            throw e;
        }
        catch (SQLException e) {
            logger.error("Cannot connect to database:" + e.getMessage());
            throw e;
        }       
    }

    /**
     * Creates connection access to repository classes
     * 
     * @return Connection to the database
     */
    public static Connection getConnection() {
        return con;
    }
}
