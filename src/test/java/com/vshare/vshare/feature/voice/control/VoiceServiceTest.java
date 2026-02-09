package com.vshare.vshare.feature.voice.control;


import com.vshare.vshare.common.service.StorageService;
import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.user.entity.User;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.boundary.dto.response.VoicePageResponse;
import com.vshare.vshare.feature.voice.entity.VoicePost;
import com.vshare.vshare.feature.voice.repo.VoicePostRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoiceServiceTest {

    @InjectMocks
    VoicePostService voicePostService;

    @Mock
    VoicePostRepo voicePostRepo;

    @Mock
    StorageService storageService;

    @Mock
    UserService userService;

    @Mock
    S3PresignerService s3PresignerService;

    private VoicePostDto voicePostDto;


    @BeforeEach
    void init() {

        voicePostDto = new VoicePostDto();
        voicePostDto.setTitle("abc");

        byte[] audioContent = "fake-audio-data".getBytes();
        MockMultipartFile audioFile = new MockMultipartFile(
                "audio",
                "test-audio.mp3",
                "audio/mpeg",
                audioContent
        );

        voicePostDto.setAudio(audioFile);
    }

    @Test
    void create_SaveAudioToS3Fail_ResponseError() throws IOException {

        when(storageService.uploadAudio(any())).thenThrow(new IOException("S3 Upload Failed"));

        ResponseStatusException exceptionResponse = assertThrows(ResponseStatusException.class, () -> voicePostService.create(voicePostDto));

        assertEquals("Lưu voice thất bại", exceptionResponse.getReason());
        verifyNoInteractions(voicePostRepo);
    }


    @Test
    void create_SaveSuccessfully() throws IOException {

        VoicePost mockVoicePost = new VoicePost();
        VoicePostDto mockVoicePostDto = new VoicePostDto();
        when(storageService.uploadAudio(any())).thenReturn("StoragedFileName");
        when(voicePostRepo.save(any())).thenReturn(mockVoicePost);
        when(userService.getUserFromSecurityContext()).thenReturn(any());

        voicePostService.create(mockVoicePostDto);

        verify(voicePostRepo, times(1)).save(any());
        verify(storageService, times(1)).uploadAudio(any());
    }

    @Test
    void getByPage_ValidPageAndSize_ResponseVoicePage() throws MalformedURLException {

        // given
        int page = 0;
        int size = 2;
        String userAccount = "testuser@example.com";

        // Create a mock User object to be returned by the user service
        User mockUser = User.builder()
                .account(userAccount)
                .build();

        when(userService.getUserFromSecurityContext()).thenReturn(mockUser);
        when(voicePostRepo.findByUserAccount(eq(userAccount), any(Pageable.class))).thenReturn(createMockVoicePostPageWithTwoItems());
        when(s3PresignerService.getAudioUrl(anyString())).thenReturn("https://s3.fake-url.com/audio.mp3");

        //action
        VoicePageResponse voicePageResponse = voicePostService.getByPage(page, size);

        //assert
        assertEquals(1, voicePageResponse.getTotalPage());
        assertEquals(2, voicePageResponse.getVoiceResponses().size());
        assertEquals("Second Mock Post", voicePageResponse.getVoiceResponses().get(1).getTitle());

    }

    private Page<VoicePost> createMockVoicePostPageWithTwoItems() {
        // 1. Create a mock user for the posts
        User mockUser = User.builder().id(UUID.randomUUID().toString())
                .nickName("testuser")
                .account("testuser@example.com")
                .build();

        // 2. Create a list with two distinct VoicePost objects
        List<VoicePost> voicePosts = List.of(
                VoicePost.builder()
                        .id(UUID.randomUUID().toString())
                        .title("First Mock Post")
                        .fileName("first_mock.mp3")
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .totalLike(15)
                        .user(mockUser)
                        .build(),
                VoicePost.builder()
                        .id(UUID.randomUUID().toString())
                        .title("Second Mock Post")
                        .fileName("second_mock.mp3")
                        .createdAt(LocalDateTime.now())
                        .totalLike(20)
                        .user(mockUser)
                        .build()
        );

        // 3. Define the page request and total elements
        Pageable pageable = PageRequest.of(0, 2);
        long totalElements = 2;

        // 4. Return a new PageImpl containing the mock data
        return new PageImpl<>(voicePosts, pageable, totalElements);
    }

    @Test
    void getByPage_InvalidPageAndSize_ResponseVoicePage() throws MalformedURLException {

        // given
        int page = -1;
        int size = -1;
        String userAccount = "testuser@example.com";

        // Create a mock User object to be returned by the user service
        User mockUser = User.builder()
                .account(userAccount)
                .build();

        when(userService.getUserFromSecurityContext()).thenReturn(mockUser);
        when(voicePostRepo.findByUserAccount(eq(userAccount), any(Pageable.class))).thenReturn(createMockVoicePostPageWithTwoItems());
        when(s3PresignerService.getAudioUrl(anyString())).thenReturn("https://s3.fake-url.com/audio.mp3");

        //action
        VoicePageResponse voicePageResponse = voicePostService.getByPage(page, size);

        //assert
        assertEquals(1, voicePageResponse.getTotalPage());
        assertEquals(2, voicePageResponse.getVoiceResponses().size());
        assertEquals("Second Mock Post", voicePageResponse.getVoiceResponses().get(1).getTitle());

    }

}
