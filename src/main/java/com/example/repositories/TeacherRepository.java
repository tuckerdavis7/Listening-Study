package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.example.configurations.DatabaseConfiguration;

public class TeacherRepository {
    public ResultSet getTeacherID(int userID) throws SQLException {
        String query = "SELECT ID FROM teachermaster WHERE user_id = ?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();

        return rs;
    }

    /**
     * Removes a teacher from all classes by setting their active tag to inactive.
     *
     * @param teacher a map containing the teacher's data, including the user ID
     * @throws SQLException if the SQL query encounters an error during execution
     */
    public void removeTeacherFromClasses(Map<String, Object> teacher) throws SQLException {
        //query for teacherMaster
        String query = "UPDATE teacherMaster set isActive = 0 where user_id = ?";

        Object id = teacher.get("id");
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, (int)Double.parseDouble(id.toString()));
        pstmt.executeUpdate();
    }

    public ResultSet getTeacherByEmail(String teacherEmail) throws SQLException {
        String query = "SELECT * FROM teachermaster WHERE Email = ?";
        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setString(1, teacherEmail);
        ResultSet rs = pstmt.executeQuery();

       return rs;
    }
}
