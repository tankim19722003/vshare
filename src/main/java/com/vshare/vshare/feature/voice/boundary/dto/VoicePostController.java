package com.vshare.vshare.feature.voice.boundary.dto;


import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.control.VoicePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voice-post")
public class VoicePostController {

    private final VoicePostService voicePostService;

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ApiResponse<String> create(@Valid @ModelAttribute  VoicePostDto voicePostDto) {
        voicePostService.create(voicePostDto);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Tạo voice thành công")
                .build();
    }

}
