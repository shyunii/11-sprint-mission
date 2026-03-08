package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.*;

public class FileMessageRepository implements MessageRepository {

    private final File file;

    public FileMessageRepository() {
        this.file = new File("messages.ser");
    }

    @Override
    public Message save(Message message) {
        Map<UUID, Message> data = load();
        data.put(message.getId(), message);
        write(data);
        return message;
    }

    @Override
    public Message findById(UUID id) {
        return load().get(id);
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Message> data = load();
        data.remove(id);
        write(data);
    }

    private void write(Map<UUID, Message> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Message> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Message>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}