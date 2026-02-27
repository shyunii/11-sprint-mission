package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String channelName;
    private String description;

    public Channel(String channelName, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.channelName = channelName;
        this.description = description;
    }

    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getChannelName() { return channelName; }
    public String getDescription() { return description; }

    public void update(String channelName, String description) {
        this.channelName = channelName;
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }
}