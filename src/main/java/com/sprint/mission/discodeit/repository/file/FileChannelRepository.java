package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.io.*;
import java.util.*;

public class FileChannelRepository implements ChannelRepository {

    private final File file;

    public FileChannelRepository() {
        this.file = new File("channels.ser");
    }

    @Override
    public Channel save(Channel channel) {
        Map<UUID, Channel> data = load();
        data.put(channel.getId(), channel);
        write(data);
        return channel;
    }

    @Override
    public Channel findById(UUID id) {
        return load().get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(load().values());
    }

    @Override
    public void delete(UUID id) {
        Map<UUID, Channel> data = load();
        data.remove(id);
        write(data);
    }

    private void write(Map<UUID, Channel> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, Channel> load() {
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, Channel>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}