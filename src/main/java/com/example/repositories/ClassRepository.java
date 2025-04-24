package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.configurations.DatabaseConfiguration;
import com.example.services.ModeratorService;

public class ClassRepository {
  private static final Logger logger = LoggerFactory.getLogger(ClassRepository.class);

      public ResultSet getClasslist(int teacherID) throws SQLException {
        String query = "SELECT * FROM class_overview WHERE teacher_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, teacherID);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }

      public ResultSet getAllClasses() throws SQLException {
        String query = "SELECT * FROM class";
        logger.info("Here 1");
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        //pstmt.setInt(1, 0);
        ResultSet rs = pstmt.executeQuery();
        logger.info("Here 2");

       return rs;
      }
    
}
