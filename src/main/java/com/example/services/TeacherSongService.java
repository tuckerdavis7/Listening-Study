package com.example.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.repositories.PlaylistSongRepository;
import com.example.repositories.SongRepository;
import com.example.services.implementations.SongImplementation;
import com.sun.net.httpserver.HttpExchange;

public class TeacherSongService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherSongService.class);
    SongRepository songRepository = new SongRepository();
    PlaylistSongRepository playlistSongRepository = new PlaylistSongRepository();
    SongImplementation songImplementation = new SongImplementation();

    public String addSong(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getParameters(exchange);
        
        int playlistID = ((Number)songData.get("playlistID")).intValue();
        String songName = (String)songData.get("name");
        String songComposer = (String)songData.get("composer");
        String songYear = (String)songData.get("year");
        String songURL = (String)songData.get("url");
        int userDefinedTimestamp = songImplementation.convertTimeToSeconds((String)songData.get("timestamp"));

        int songMostReplayedTimestamp = songImplementation.getMostReplayedTimestamp(songURL);
        String songVideoID = songImplementation.extractVideoId(songURL);
        
        String responseString = "";
        try {
            songRepository.commitSongData(songName, songComposer, songYear, songVideoID, songMostReplayedTimestamp);

            ResultSet rs = songRepository.getSongID(songVideoID);
            int songID = 0;
            if (rs.next())
                songID = rs.getInt("ID");

            playlistSongRepository.addToPlaylist(playlistID, songID, userDefinedTimestamp);

            responseString = super.formatJSON("success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong of TeacherSongService: " + e.getMessage());
        }
        return responseString;
    }
}
