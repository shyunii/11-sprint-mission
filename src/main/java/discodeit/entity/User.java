package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    // 1. 필드 선언
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String name;
    private String email;

    // 2. 생성자
    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.email = email;
    }

    // 3. Getter 함수
    public UUID getId() { return id; }
    public Long getCreatedAt() { return createdAt; }
    public Long getUpdatedAt() { return updatedAt; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // 4. Update 함수
    public void update(String name, String email) {
        this.name = name;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }
}