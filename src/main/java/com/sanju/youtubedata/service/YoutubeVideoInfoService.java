package com.sanju.youtubedata.service;


import com.sanju.youtubedata.entity.YouTubeVideoInfo;

import java.util.List;

public interface YoutubeVideoInfoService {
    void save(YouTubeVideoInfo videoInfo);
    void update(YouTubeVideoInfo videoInfo);
    YouTubeVideoInfo getByVideoId(String videoId);
    YouTubeVideoInfo get(long id);
    List<YouTubeVideoInfo> getAll();
}
