package com.example.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.configuration.DatabaseConfiguration;

public class PlaylistSongRepository {
    public List<Integer> getSongIDs(int playListID) throws SQLException {
        List<Integer> songIDs = new ArrayList<>();
        String query = "SELECT * FROM playlistsongs WHERE playlistID =?";

        PreparedStatement pstmt = DatabaseConfiguration.getConnection().prepareStatement(query);
        pstmt.setInt(1, playListID);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            songIDs.add(rs.getInt("songID"));
        }

       return songIDs;        

    }

}
