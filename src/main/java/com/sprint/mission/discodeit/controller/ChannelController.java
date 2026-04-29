package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelUpdateParam;
import com.sprint.mission.discodeit.dto.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/public")
    public ChannelDto createPublic(@RequestBody PublicChannelCreateRequest request) {
        return channelService.createPublic(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/private")
    public ChannelDto createPrivate(@RequestBody PrivateChannelCreateRequest request) {
        return channelService.createPrivate(request);
    }

    @PatchMapping(value = "/{channelId}")
    public ChannelDto update(@PathVariable UUID channelId,
                                   @RequestBody ChannelUpdateRequest request) {
        return channelService.update(new ChannelUpdateParam(channelId, request));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{channelId}")
    public void delete(@PathVariable UUID channelId) {
        channelService.delete(channelId);
    }

    @GetMapping
    public List<ChannelDto> findAllByUserId(@RequestParam UUID userId) {
        return channelService.findAllByUserId(userId);
    }
}