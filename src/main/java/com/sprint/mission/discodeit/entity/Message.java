package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private UUID userId;
    private UUID channelId;
    private String content;

    public Message(UUID userId, UUID channelID, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelID;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }
    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }
    public String getContent() {
        return content;
    }
}
