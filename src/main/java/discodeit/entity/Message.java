package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private UUID authorId;
    private UUID channelId;
    private String content;

    public Message(UUID authorId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.authorId = authorId;
        this.channelId = channelId;
        this.content = content;
    }

    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public UUID getAuthorId() { return authorId; }
    public UUID getChannelId() { return channelId; }
    public String getContent() { return content; }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}