package com.sanju.youtubedata.service;

public interface YoutubeApiService {
    String crawlYoutubeVideoInfo(String keyword, long pageToCrawl);
}
