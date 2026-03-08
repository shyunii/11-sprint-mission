package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final File file;

    public FileUserRepository() {
        this.file = new File("users.ser");
    }

    @Override
    public User save(User user) {
        Map<UUID, User> data = load();
        data.put(user.getId(), user);
        write(data);
        return user;
    }

    @Override
    public User findById(UUID id) {
        return load().get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, User> data = load();
        data.remove(id);
        write(data);
    }

    private void write(Map<UUID, User> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}