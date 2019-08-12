package com.sanju.youtubedata.repository;

import com.sanju.youtubedata.entity.YouTubeVideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeVideoInfoRepository extends JpaRepository<YouTubeVideoInfo,Long> {
    YouTubeVideoInfo findByVideoId(String videoId);
}
