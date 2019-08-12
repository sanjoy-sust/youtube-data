package com.sanju.youtubedata.service;



import com.sanju.youtubedata.entity.CrawlingInfo;

import java.util.List;

public interface CrawlingInfoService {
    void save(CrawlingInfo crawlingInfo);
    void update(CrawlingInfo crawlingInfo);
    CrawlingInfo get(long id);
    CrawlingInfo getBySearchKey(String searchKey);
    List<CrawlingInfo> getAll();
}
