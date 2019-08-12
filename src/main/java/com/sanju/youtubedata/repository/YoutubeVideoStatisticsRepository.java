package com.sanju.youtubedata.repository;

import com.sanju.youtubedata.entity.YoutubeVideoStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeVideoStatisticsRepository extends JpaRepository<YoutubeVideoStatistics,Long> {
}
