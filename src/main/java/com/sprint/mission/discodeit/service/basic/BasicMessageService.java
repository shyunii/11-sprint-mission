package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageMapper messageMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @Transactional
    public MessageDto create(MessageCreateRequest request) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 사용자입니다."));

        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채널입니다."));

        List<BinaryContent> attachments = new ArrayList<>();

        if (request.attachments() != null) {
            for (BinaryContentCreateRequest attachmentRequest : request.attachments()) {
                BinaryContent attachment = new BinaryContent(
                        attachmentRequest.fileName(),
                        attachmentRequest.bytes() == null ? 0 : attachmentRequest.bytes().length,
                        attachmentRequest.contentType()
                );

                BinaryContent savedAttachment = binaryContentRepository.save(attachment);

                if (attachmentRequest.bytes() != null) {
                    binaryContentStorage.put(savedAttachment.getId(), attachmentRequest.bytes());
                }

                attachments.add(savedAttachment);
            }
        }

        Message message = new Message(author, channel, request.content());
        message.updateAttachments(attachments);

        Message savedMessage = messageRepository.save(message);
        return messageMapper.toDto(savedMessage);
    }

    @Override
    public Optional<MessageDto> find(UUID id) {
        return messageRepository.findByIdWithDetails(id)
                .map(messageMapper::toDto);
    }

    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, String cursor, int size) {
        int pageSize = size <= 0 ? 50 : Math.min(size, 50);

        Instant cursorCreatedAt = null;
        UUID cursorId = null;

        if (cursor != null && !cursor.isBlank()) {
            try {
                String[] parts = cursor.split("_", 2);
                cursorCreatedAt = Instant.parse(parts[0]);
                cursorId = UUID.fromString(parts[1]);
            } catch (Exception e) {
                throw new InvalidException("cursor 형식이 올바르지 않습니다.");
            }
        }

        List<UUID> ids = messageRepository.findPageIdsByChannelIdAndCursor(
                channelId,
                cursorCreatedAt,
                cursorId,
                PageRequest.of(0, pageSize + 1)
        );

        boolean hasNext = ids.size() > pageSize;
        if (hasNext) {
            ids = ids.subList(0, pageSize);
        }

        if (ids.isEmpty()) {
            return new PageResponse<>(List.of(), null, pageSize);
        }

        List<MessageDto> content = messageRepository.findAllWithDetailsByIdIn(ids).stream()
                .map(messageMapper::toDto)
                .toList();

        String nextCursor = null;
        if (hasNext) {
            MessageDto last = content.get(content.size() - 1);
            nextCursor = last.createdAt().toString() + "_" + last.id();
        }

        return new PageResponse<>(content, nextCursor, pageSize);
    }

    @Override
    @Transactional
    public MessageDto update(MessageUpdateParam param) {
        Message message = messageRepository.findById(param.id())
                .orElseThrow(() -> new NotFoundException("해당 메시지가 존재하지 않습니다."));

        message.update(param.request().newContent());
        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 메시지가 존재하지 않습니다."));

        messageRepository.delete(message);

        for (BinaryContent attachment : message.getAttachments()) {
            binaryContentRepository.deleteById(attachment.getId());
            binaryContentStorage.delete(attachment.getId());
        }
    }
}