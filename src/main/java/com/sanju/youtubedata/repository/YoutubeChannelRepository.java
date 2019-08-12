package com.sanju.youtubedata.repository;


import com.sanju.youtubedata.entity.YoutubeChannelInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannelInfo,Long> {
    public YoutubeChannelInfo findByChannelId(String channelId);
}
