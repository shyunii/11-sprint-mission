package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    public JCFMessageService() {
        this.data = new HashMap<>();
    }

    @Override
    public Message createMessage(UUID authorId, UUID channelId, String content) {
        Message message = new Message(authorId, channelId, content);
        data.put(message.getId(), message);
        return message;
    }

    @Override
    public Message getMessage(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Message> getAllMessages() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message updateMessage(UUID id, String content) {
        Message message = data.get(id);
        if (message != null) {
            message.update(content);
            data.put(id, message);
        }
        return message;
    }

    @Override
    public void deleteMessage(UUID id) {
        data.remove(id);
    }
}