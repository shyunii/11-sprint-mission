package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.exception.InvalidException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.exception.StorageException;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
public class BinaryController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    public BinaryController(BinaryContentService binaryContentService,
                            BinaryContentStorage binaryContentStorage) {
        this.binaryContentService = binaryContentService;
        this.binaryContentStorage = binaryContentStorage;
    }

    @GetMapping(value = "/{binaryContentId}/download")
    public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));

        return binaryContentStorage.download(binaryContent);
    }

    @GetMapping
    public List<BinaryContentDto> findAllByIdIn(@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @GetMapping(value = "/{binaryContentId}")
    public ResponseEntity<byte[]> findById(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));

        try (var inputStream = binaryContentStorage.get(binaryContentId)) {
            byte[] bytes = inputStream.readAllBytes();
            return ResponseEntity.ok()
                    .header("Content-Type", binaryContent.contentType())
                    .body(bytes);
        } catch (Exception e) {
            throw new StorageException("파일 조회 중 오류가 발생했습니다.", e);
        }
    }
}