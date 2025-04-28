package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.configurations.DatabaseConfiguration;

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
      String query = "SELECT * FROM class_overview";
      PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
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

  public ResultSet getTeachersByClassID(int classID) throws SQLException {
    logger.info("at getTeachersByClassID in ClassRepository");
    String query = """
                      SELECT class.*, teachermaster.*
                      FROM class
                      INNER JOIN teachermaster 
                      ON class.teacherID = teachermaster.ID 
                      WHERE class.ID = ?
                    """;

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, classID);
    ResultSet rs = pstmt.executeQuery();

    return rs;
  } 

  public void addClass(String className, int teacherID) throws SQLException {
    logger.info("at addClass in ClassRepository");
    String query = "INSERT INTO class (className, teacherID) VALUES (?, ?)";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);

      pstmt.setString(1, className);
      pstmt.setInt(2, teacherID);
      pstmt.executeUpdate();
  }

  public void removeTeacherFromClass(int classID) throws SQLException {
    logger.info("at removeTeacherFromClass in ClassRepository");
    String query = "UPDATE class SET teacherID = NULL WHERE ID=?";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, classID);
    pstmt.executeUpdate();
  }

  public void renameClass(Object classID, Object newClassname) throws SQLException {
    String query = "UPDATE class SET className = ? WHERE ID = ?";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setString(1, (String) newClassname);
    pstmt.setInt(2, (Integer) classID);
    pstmt.executeUpdate();
  }

  public void addTeacherToClass(int classID, String teacherEmail) throws SQLException {
    logger.info("at addTeacherToClass in ClassRepository");
    String query = """
                      UPDATE class c
                      SET c.teacherID = (SELECT tm.ID FROM teachermaster tm WHERE tm.Email = ?)
                      WHERE c.ID = ?
                      AND c.teacherID is NULL
                    """;

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setString(1, teacherEmail);
    pstmt.setInt(2, classID);
    pstmt.executeUpdate();
  }

  public ResultSet getStudentsByClassID(int classID) throws SQLException {
    logger.info("at getTeachersByClassID in ClassRepository");
    String query = "SELECT studentID, studentEmail, studentFirstname, studentLastname FROM view_class WHERE classID = ?";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, classID);
    ResultSet rs = pstmt.executeQuery();

    return rs;
  }

  public void deleteClass(int classID) throws SQLException {
    logger.info("at deleteClass in ClassRepository");
    String query = "DELETE FROM class WHERE ID = ?";

    PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
    pstmt.setInt(1, classID);
    pstmt.executeUpdate();
  }

}

