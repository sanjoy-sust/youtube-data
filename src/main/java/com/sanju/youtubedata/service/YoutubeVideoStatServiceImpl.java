package com.sanju.youtubedata.service;


import com.sanju.youtubedata.entity.YoutubeVideoStatistics;
import com.sanju.youtubedata.repository.YoutubeVideoStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubeVideoStatServiceImpl implements YoutubeVideoStatService {

    @Autowired
    private YoutubeVideoStatisticsRepository youtubeVideoStatisticsRepository;

    @Override
    public void save(YoutubeVideoStatistics videoStatistics) {
        youtubeVideoStatisticsRepository.save(videoStatistics);
    }

    @Override
    public void update(YoutubeVideoStatistics videoStatistics) {
        youtubeVideoStatisticsRepository.save(videoStatistics);
    }

    @Override
    public YoutubeVideoStatistics get(long id) {
        return youtubeVideoStatisticsRepository.findById(id).get();
    }

    @Override
    public List<YoutubeVideoStatistics> getAll() {
        return youtubeVideoStatisticsRepository.findAll();
    }
}
