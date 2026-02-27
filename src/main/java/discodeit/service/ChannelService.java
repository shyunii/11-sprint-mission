package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(String channelName, String description);
    Channel getChannel(UUID id);
    List<Channel> getAllChannels();
    Channel updateChannel(UUID id, String channelName, String description);
    void deleteChannel(UUID id);
}