package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ChannelDto createPublic(PublicChannelCreateRequest request) {
        Channel channel = new Channel(request.name(), request.description());
        Channel savedChannel = channelRepository.save(channel);
        return channelMapper.toDto(savedChannel, List.of(), null);
    }

    @Override
    @Transactional
    public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
        List<User> participants = request.participantIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다.")))
                .toList();

        Channel channel = Channel.createPrivateChannel();
        Channel savedChannel = channelRepository.save(channel);

        for (User user : participants) {
            ReadStatus readStatus = new ReadStatus(user, savedChannel, Instant.now());
            readStatusRepository.save(readStatus);
        }

        List<UserDto> participantDtos = participants.stream()
                .map(userMapper::toDto)
                .toList();

        return channelMapper.toDto(savedChannel, participantDtos, null);
    }

    @Override
    public Optional<ChannelDto> find(UUID id) {
        return channelRepository.findById(id)
                .map(channel -> {
                    Map<UUID, Instant> lastMessageAtMap = messageRepository
                            .findLastMessageTimesByChannelIds(List.of(channel.getId()))
                            .stream()
                            .collect(Collectors.toMap(
                                    row -> (UUID) row[0],
                                    row -> (Instant) row[1]
                            ));

                    Map<UUID, List<UserDto>> participantsMap = readStatusRepository
                            .findAllWithUserByChannelIds(List.of(channel.getId()))
                            .stream()
                            .collect(Collectors.groupingBy(
                                    rs -> rs.getChannel().getId(),
                                    Collectors.mapping(rs -> userMapper.toDto(rs.getUser()), Collectors.toList())
                            ));

                    return channelMapper.toDto(
                            channel,
                            participantsMap.getOrDefault(channel.getId(), List.of()),
                            lastMessageAtMap.get(channel.getId())
                    );
                });
    }

    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findVisibleChannelsByUserId(userId);

        if (channels.isEmpty()) {
            return List.of();
        }

        List<UUID> channelIds = channels.stream()
                .map(Channel::getId)
                .toList();

        Map<UUID, Instant> lastMessageAtMap = messageRepository.findLastMessageTimesByChannelIds(channelIds).stream()
                .collect(Collectors.toMap(
                        row -> (UUID) row[0],
                        row -> (Instant) row[1]
                ));

        Map<UUID, List<UserDto>> participantsMap = readStatusRepository.findAllWithUserByChannelIds(channelIds).stream()
                .collect(Collectors.groupingBy(
                        rs -> rs.getChannel().getId(),
                        Collectors.mapping(rs -> userMapper.toDto(rs.getUser()), Collectors.toList())
                ));

        return channels.stream()
                .map(channel -> channelMapper.toDto(
                        channel,
                        participantsMap.getOrDefault(channel.getId(), List.of()),
                        lastMessageAtMap.get(channel.getId())
                ))
                .toList();
    }

    @Override
    @Transactional
    public ChannelDto update(ChannelUpdateParam param) {
        Channel channel = channelRepository.findById(param.id())
                .orElseThrow(() -> new NotFoundException("해당 채널이 존재하지 않습니다."));

        if (channel.getType().name().equals("PRIVATE")) {
            throw new InvalidException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.update(
                param.request().newName(),
                param.request().newDescription()
        );

        return find(channel.getId())
                .orElseThrow(() -> new NotFoundException("해당 채널이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 채널이 존재하지 않습니다."));

        channelRepository.delete(channel);
    }
}