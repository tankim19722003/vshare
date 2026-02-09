package com.vshare.vshare.feature.voice.control;

import com.vshare.vshare.common.service.StorageService;
import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.boundary.dto.response.VoicePageResponse;
import com.vshare.vshare.feature.voice.boundary.dto.response.VoiceResponse;
import com.vshare.vshare.feature.voice.entity.VoicePost;
import com.vshare.vshare.feature.voice.repo.VoicePostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoicePostService {

    private final VoicePostRepo voicePostRepo;
    private final StorageService storageService;
    private final UserService userService;
    private final S3PresignerService s3PresignerService;

    @Value("${AWS_S3_REGION}")
    private String awsRegion;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

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

    public VoicePageResponse getByPage(int page, int size) {

//        set page and size default if page or size is less than 0
        if (page < 0 || size < 0) {
            page = 0;
            size = 10;
        }

        String account = userService.getUserFromSecurityContext().getAccount();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<VoicePost> voicePostPage = voicePostRepo.findByUserAccount(account, pageable);

        return VoicePageResponse.builder()
                .voiceResponses(voicePostPage.map(this::mapToVoiceResponse).toList())
                .totalPage(voicePostPage.getTotalPages())
                .build();
    }

    private VoiceResponse mapToVoiceResponse(VoicePost voicePost) {
        return VoiceResponse.builder()
                .id(voicePost.getId())
                .title(voicePost.getTitle())
                .totalLike(voicePost.getTotalLike())
                .createdAt(voicePost.getCreatedAt())
                .voiceUrl(s3PresignerService.getAudioUrl(voicePost.getFileName()))
                .build();
    }

}
