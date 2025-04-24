package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configurations.DatabaseConfiguration;

public class ClassRepository {
  public ResultSet getClasslist(int teacherID) throws SQLException {
    String query = "SELECT * FROM class_overview WHERE teacher_id = ?";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, teacherID);
    ResultSet rs = pstmt.executeQuery();

    return rs;
  }


  public void removeTeacherFromClasses(Map<String, Object> teacher) throws SQLException {
    String query = "UPDATE class SET teacherID = NULL WHERE teacherID = (SELECT tm.ID FROM teacherMaster tm WHERE tm.user_id = ?)";

    Object id = teacher.get("id");
    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, (int)Double.parseDouble(id.toString()));
    pstmt.executeUpdate();

    //query for teacherMaster
    query = "UPDATE teacherMaster set isActive = 0 where user_id = ?";

    PreparedStatement pstmt2 = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt2.setInt(1, (int)Double.parseDouble(id.toString()));
    pstmt2.executeUpdate();
  }
    
}
