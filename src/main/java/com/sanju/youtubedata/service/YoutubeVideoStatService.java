package com.sanju.youtubedata.service;


import com.sanju.youtubedata.entity.YoutubeVideoStatistics;

import java.util.List;

public interface YoutubeVideoStatService {
    void save(YoutubeVideoStatistics videoStatistics);
    void update(YoutubeVideoStatistics videoInfo);
    YoutubeVideoStatistics get(long id);
    List<YoutubeVideoStatistics> getAll();
}
