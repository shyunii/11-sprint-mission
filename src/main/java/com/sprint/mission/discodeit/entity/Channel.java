package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updateAt;

    private String name;
    private String description;

    public Channel(String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;
        this.name = name;
        this.description = description;
    }

    public void updateAt(String name, String description) {
        this.name = name;
        this.description = description;
        this.updateAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
