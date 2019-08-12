package com.sanju.youtubedata.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "youtube_channel")
@Data
@EqualsAndHashCode(callSuper=true)
public class YoutubeChannelInfo extends BaseEntity{

    @Column(name = "channel_id")
    private String channelId;
    @Column(name = "name")
    private String name;
    @Column(name = "subscription_count")
    private long subscriptionCount;
}
