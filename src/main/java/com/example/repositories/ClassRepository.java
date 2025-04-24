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

/**
 * Removes a teacher from all classes by setting their teacherID to NULL.
 *
 * @param teacher a map containing the teacher's data, including the user ID
 * @throws SQLException if the SQL query encounters an error during execution
 */
  public void removeTeacherFromClasses(Map<String, Object> teacher) throws SQLException {
    String query = "UPDATE class SET teacherID = NULL WHERE teacherID = (SELECT tm.ID FROM teacherMaster tm WHERE tm.user_id = ?)";

    Object id = teacher.get("id");
    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, (int)Double.parseDouble(id.toString()));
    pstmt.executeUpdate();
  }
    
}
