package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    // 생성 (Create)
    User createUser(String name, String email);
    // 단건 조회 (Read)
    User getUser(UUID id);
    // 다건 조회 (Read All)
    List<User> getAllUsers();
    // 수정 (Update)
    User updateUser(UUID id, String name, String email);
    // 삭제 (Delete)
    void deleteUser(UUID id);
}