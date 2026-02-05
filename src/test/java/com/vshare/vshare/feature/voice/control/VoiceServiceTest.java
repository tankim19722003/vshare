package com.vshare.vshare.feature.voice.control;


import com.vshare.vshare.common.service.StorageService;
import com.vshare.vshare.feature.user.control.UserService;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.entity.VoicePost;
import com.vshare.vshare.feature.voice.repo.VoicePostRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

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
    void create_saveSuccessfully() throws IOException {

        VoicePost mockVoicePost = new VoicePost();
        VoicePostDto mockVoicePostDto = new VoicePostDto();
        when(storageService.uploadAudio(any())).thenReturn("StoragedFileName");
        when(voicePostRepo.save(any())).thenReturn(mockVoicePost);
        when(userService.getUserFromSecurityContext()).thenReturn(any());

        voicePostService.create(mockVoicePostDto);

        verify(voicePostRepo, times(1)).save(any());
        verify(storageService, times(1)).uploadAudio(any());
    }
}
