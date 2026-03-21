package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel create(String name, String description) {
        Channel channel = new Channel(name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, String name, String description) {
        Optional<Channel> optionalChannel = channelRepository.findById(id);

        if (optionalChannel.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }

        Channel channel = optionalChannel.get();
        channel.update(name, description);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        if (channelRepository.findById(id).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }

        channelRepository.delete(id);
    }
}