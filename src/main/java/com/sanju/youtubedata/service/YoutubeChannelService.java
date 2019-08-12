package com.sanju.youtubedata.service;


import com.sanju.youtubedata.entity.YoutubeChannelInfo;

import java.util.List;

public interface YoutubeChannelService {

    void save(YoutubeChannelInfo channelInfo);
    void update(YoutubeChannelInfo channelInfo);
    YoutubeChannelInfo get(long id);
    YoutubeChannelInfo getByChannelId(String channelId);
    List<YoutubeChannelInfo> getAll();

}
