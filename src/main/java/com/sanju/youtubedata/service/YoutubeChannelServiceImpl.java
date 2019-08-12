package com.sanju.youtubedata.service;


import com.sanju.youtubedata.entity.YoutubeChannelInfo;
import com.sanju.youtubedata.repository.YoutubeChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubeChannelServiceImpl implements YoutubeChannelService {

    @Autowired
    YoutubeChannelRepository youtubeChannelRepository;


    @Override
    public void save(YoutubeChannelInfo channelInfo) {
        youtubeChannelRepository.save(channelInfo);
    }

    @Override
    public void update(YoutubeChannelInfo channelInfo) {
        youtubeChannelRepository.save(channelInfo);
    }

    @Override
    public YoutubeChannelInfo get(long id) {
        return youtubeChannelRepository.findById(id).get();
    }

    @Override
    public YoutubeChannelInfo getByChannelId(String channelId) {
        return youtubeChannelRepository.findByChannelId(channelId);
    }

    @Override
    public List<YoutubeChannelInfo> getAll() {
        return youtubeChannelRepository.findAll();
    }
}
