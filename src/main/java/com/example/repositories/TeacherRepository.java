package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configurations.DatabaseConfiguration;

/**
 * Repository class to execute queries on the teacherMaster table.
 */
public class TeacherRepository {

    /**
     * Retrieves the teacher ID associated with a specific user ID.
     *
     * @param userID The user ID linked to the teacher
     * @return ResultSet containing the teacher ID
     * @throws SQLException When the query fails to execute
     */
    public ResultSet getTeacherID(int userID) throws SQLException {
        String query = "SELECT ID FROM teachermaster WHERE user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        return pstmt.executeQuery();
    }

    /**
     * Removes a teacher from all classes by marking them as inactive in the teacherMaster table.
     *
     * @param teacher A map containing the teacher's data, including the user ID (key: "id")
     * @throws SQLException When the query fails to execute
     */
    public void removeTeacherFromClasses(Map<String, Object> teacher) throws SQLException {
        String query = "UPDATE teacherMaster SET isActive = 0 WHERE user_id = ?";

        Object id = teacher.get("id");
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (int) Double.parseDouble(id.toString()));
        pstmt.executeUpdate();
    }

    /**
     * Retrieves teacher details using their email address.
     *
     * @param teacherEmail The email of the teacher
     * @return ResultSet containing the teacher's record
     * @throws SQLException When the query fails to execute
     */
    public ResultSet getTeacherByEmail(String teacherEmail) throws SQLException {
        String query = "SELECT * FROM teachermaster WHERE Email = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, teacherEmail);
        return pstmt.executeQuery();
    }
}
