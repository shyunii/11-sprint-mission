package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Message implements Serializable{
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private UUID userId;
    private UUID channelId;
    private String content;

    public Message(UUID userId, UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
