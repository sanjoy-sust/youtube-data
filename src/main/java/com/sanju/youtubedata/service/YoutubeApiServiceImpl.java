package com.sanju.youtubedata.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.model.*;

import com.sanju.youtubedata.entity.CrawlingInfo;
import com.sanju.youtubedata.entity.YouTubeVideoInfo;
import com.sanju.youtubedata.entity.YoutubeChannelInfo;
import com.sanju.youtubedata.entity.YoutubeVideoStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class YoutubeApiServiceImpl implements YoutubeApiService {

    @Autowired
    private Environment env;

    private static final long NUMBER_OF_VIDEOS_RETURNED = 50;

    private YouTube youtube;

    private long count = 0;

    @Autowired
    YoutubeVideoInfoService youtubeVideoInfoService;

    @Autowired
    YoutubeVideoStatService youtubeVideoStatService;

    @Autowired
    YoutubeChannelService youtubeChannelService;

    @Autowired
    CrawlingInfoService crawlingInfoService;


    @Override
    @Async("threadPoolTaskExecutor")
    public String crawlYoutubeVideoInfo(String keyword,long pageToCrawl) {
        getYoutubeVideoList(keyword,pageToCrawl);
        return "Youtube video information is loading...";
    }

    @Transactional
    public List<Object> getYoutubeVideoList(String queryTerm,long pageToCrawl) {

        try {

            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("YoutubeVideoInfo")
                    .setYouTubeRequestInitializer(new YouTubeRequestInitializer(env.getProperty("youtube.apikey"))).build();

            YouTube.Search.List search = youtube.search().list("id,snippet");


            search.setQ(queryTerm);
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            for (int i = 0; i < pageToCrawl; i++) {
                String pageToken = null;
                CrawlingInfo crawlingInfo = crawlingInfoService.getBySearchKey(queryTerm);
                if (crawlingInfo != null && crawlingInfo.getNextPageToken() != null) {
                    pageToken = crawlingInfo.getNextPageToken();
                    count = crawlingInfo.getTotalCount();
                    crawlingInfo.setCurrentPageToken(pageToken);
                } else if (crawlingInfo == null) {
                    crawlingInfo = new CrawlingInfo();
                    count = 0;
                    crawlingInfo.setSearchKey(queryTerm);
                    crawlingInfo.setCurrentPageToken(null);
                }

                if (pageToken != null) {
                    search.setPageToken(pageToken);
                }


                SearchListResponse searchResponse = search.execute();

                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    extractAndSave(searchResultList.iterator(), queryTerm);
                }

                crawlingInfo.setNextPageToken(searchResponse.getNextPageToken());

                crawlingInfoService.update(crawlingInfo);

                System.out.println("Next Page token : " + searchResponse.getNextPageToken());
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private void extractAndSave(Iterator<SearchResult> iteratorSearchResults, String query) throws IOException {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();

            System.out.println("Video number = " + count + " Inserting video Information " + singleVideo.getId().getVideoId());
            count++;
            YouTubeVideoInfo youTubeVideoInfo = youtubeVideoInfoService.getByVideoId(singleVideo.getId().getVideoId());

            if (youTubeVideoInfo == null) {
                youTubeVideoInfo = new YouTubeVideoInfo();
                ResourceId rId = singleVideo.getId();

                youTubeVideoInfo.setKeyword(query);
                youTubeVideoInfo.setDescription(singleVideo.getSnippet().getDescription());
                youTubeVideoInfo.setPublishedDate(new Date(singleVideo.getSnippet().getPublishedAt().getValue()));

                if (rId.getKind().equals("youtube#video")) {
                    Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                    youTubeVideoInfo.setVideoId(rId.getVideoId());
                    youTubeVideoInfo.setTitle(singleVideo.getSnippet().getTitle());
                    youTubeVideoInfo.setThumbnailUrl(thumbnail.getUrl());

                    youTubeVideoInfo.setChannelInfo(getChannelDetailsById(singleVideo.getSnippet().getChannelId()));

                    youTubeVideoInfo.setVideoStatistics(getVideosStatistics(rId.getVideoId()));
                }

                youtubeVideoInfoService.save(youTubeVideoInfo);
            } else {
                System.out.println("Video Already exists... ");
            }


        }
    }

    private YoutubeChannelInfo getChannelDetailsById(String channelId) throws IOException {
        YouTube.Channels.List channels = youtube.channels().list("snippet, statistics");

        YoutubeChannelInfo youtubeChannelInfo = new YoutubeChannelInfo();
        youtubeChannelInfo.setChannelId(channelId);
        channels.setId(channelId);
        ChannelListResponse channelResponse = channels.execute();
        Channel c = channelResponse.getItems().get(0);

        youtubeChannelInfo.setName(c.getSnippet().getTitle());
        youtubeChannelInfo.setSubscriptionCount(c.getStatistics().getSubscriberCount().longValue());

        YoutubeChannelInfo channelInfo = youtubeChannelService.getByChannelId(channelId);

        if (channelInfo == null) {
            youtubeChannelService.save(youtubeChannelInfo);
        } else {
            return channelInfo;
        }
        return youtubeChannelInfo;
    }


    public YoutubeVideoStatistics getVideosStatistics(String id) throws IOException {
        YouTube.Videos.List list = youtube.videos().list("statistics");
        list.setId(id);
        Video v = list.execute().getItems().get(0);

        YoutubeVideoStatistics statistics = new YoutubeVideoStatistics();

        statistics.setVideoId(id);
        statistics.setLikeCount(v.getStatistics().getLikeCount() !=null ? v.getStatistics().getLikeCount().longValue():0);
        statistics.setDislikeCount(v.getStatistics().getDislikeCount() != null ? v.getStatistics().getDislikeCount().longValue():0);
        statistics.setFavouriteCount(v.getStatistics().getFavoriteCount() != null ? v.getStatistics().getFavoriteCount().longValue():0);
        statistics.setCommentCount(v.getStatistics().getCommentCount() != null ? v.getStatistics().getCommentCount().longValue() : 0);
        statistics.setViewCount(v.getStatistics().getViewCount() != null ? v.getStatistics().getViewCount().longValue() : 0);


        youtubeVideoStatService.save(statistics);

        return statistics;
    }


    public YouTubeVideoInfo getCoontentDetails(String id, YouTubeVideoInfo youTubeVideoInfo) throws IOException {
        YouTube.Videos.List list = youtube.videos().list("contentDetails");
        list.setId(id);
        Video v = list.execute().getItems().get(0);
        youTubeVideoInfo.setVideoDefinition(v.getContentDetails().getDefinition());
        youTubeVideoInfo.setVideoCaption(v.getContentDetails().getCaption());
        youTubeVideoInfo.setVideoprojection(v.getContentDetails().getProjection());
        youTubeVideoInfo.setCountryRestricted(v.getContentDetails().getCountryRestriction().toPrettyString());

        return youTubeVideoInfo;
    }
}
