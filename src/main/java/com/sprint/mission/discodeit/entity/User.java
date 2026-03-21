package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String username;
    private String email;
    private String password;

    public User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }
}