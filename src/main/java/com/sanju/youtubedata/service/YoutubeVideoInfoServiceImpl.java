package com.sanju.youtubedata.service;

import com.sanju.youtubedata.entity.YouTubeVideoInfo;
import com.sanju.youtubedata.repository.YoutubeVideoInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubeVideoInfoServiceImpl implements YoutubeVideoInfoService {

    @Autowired
    private YoutubeVideoInfoRepository youtubeVideoInfoRepository;

    @Override
    public void save(YouTubeVideoInfo videoInfo) {
        youtubeVideoInfoRepository.save(videoInfo);
    }

    @Override
    public void update(YouTubeVideoInfo videoInfo) {
        youtubeVideoInfoRepository.save(videoInfo);
    }

    @Override
    public YouTubeVideoInfo getByVideoId(String videoId) {
        return youtubeVideoInfoRepository.findByVideoId(videoId);
    }

    @Override
    public YouTubeVideoInfo get(long id) {
        return youtubeVideoInfoRepository.findById(id).get();
    }

    @Override
    public List<YouTubeVideoInfo> getAll() {
        return youtubeVideoInfoRepository.findAll();
    }
}
