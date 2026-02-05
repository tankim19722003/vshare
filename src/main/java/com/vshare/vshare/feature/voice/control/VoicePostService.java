package com.vshare.vshare.feature.voice.control;

import com.vshare.vshare.common.service.StorageService;
import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.entity.VoicePost;
import com.vshare.vshare.feature.voice.repo.VoicePostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoicePostService implements IVoicePost{

    private final VoicePostRepo voicePostRepo;
    private final StorageService storageService;
    private final UserService userService;

    @Override
    public void create(VoicePostDto dto) {
        log.info("Save voice with dto: {}",dto);
        saveToDb(saveToS3(dto.getAudio()), dto);
    }

    private String saveToS3(MultipartFile voicePostFile) {
        try {
            return storageService.uploadAudio(voicePostFile);
        } catch (IOException e) {
            log.error("Storage voice to S3 failed");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lưu voice thất bại");
        }
    }

    private void saveToDb(String filePath, VoicePostDto dto) {

        VoicePost voicePost = VoicePost.builder()
                .title(dto.getTitle())
                .fileName(filePath)
                .user(userService.getUserFromSecurityContext())
                .build();

        voicePostRepo.save(voicePost);
    }
}
