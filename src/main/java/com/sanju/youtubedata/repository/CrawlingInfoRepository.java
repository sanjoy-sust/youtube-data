package com.sanju.youtubedata.repository;


import com.sanju.youtubedata.entity.CrawlingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlingInfoRepository extends JpaRepository<CrawlingInfo,Long> {
    CrawlingInfo findBySearchKey(String searchKey);
}
