package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User createUser(String name, String email) {
        User newUser = new User(name, email);
        data.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User getUser(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> getAllUsers() {
       return new ArrayList<>(data.values());
    }

    @Override
    public User updateUser(UUID id, String name, String email) {
        User user = data.get(id);
        if (user != null) {
            user.update(name, email);
            data.put(id, user);
        }
        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}