package com.example.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.services.implementations.SongImplementation;
import com.sun.net.httpserver.HttpExchange;

public class TeacherSongService extends BaseService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherSongService.class);
    //SongRepository songRepository = new SongRepository();
    SongImplementation songImplementation = new SongImplementation();

    public String addSong(HttpExchange exchange) throws IOException {
        Map<String, Object> songData = super.getParameters(exchange);
        
        String playlistId = (String)songData.get("playlistId");
        String songName = (String)songData.get("name");
        String songComposer = (String)songData.get("composer");
        String songYear = (String)songData.get("year");
        String songUrl = (String)songData.get("url");
        
        int songMostReplayedTimestamp = songImplementation.getMostReplayedTimestamp(songUrl);

        String responseString = Integer.toString(songMostReplayedTimestamp);
        try {
            
            //ResultSet result = songRepository.addSongData();
            //responseString = super.formatJSON(usersList, "success");
        }
        catch (Exception e) {
            responseString = "Internal Server Error";
            logger.error("Error in addSong of TeacherSongService: " + e.getMessage());
        }
        return responseString;
    }
}
