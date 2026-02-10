package com.vshare.vshare.feature.voice.boundary;


import com.vshare.vshare.common.response.ApiResponse;
import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.boundary.dto.response.VoicePageResponse;
import com.vshare.vshare.feature.voice.control.VoicePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voice-post")
public class VoicePostController {

    private final VoicePostService voicePostService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<Void> create(@Valid @ModelAttribute  VoicePostDto voicePostDto) {
        voicePostService.create(voicePostDto);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Tạo voice thành công")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<VoicePageResponse> getUserVoicePostsByPage(
            @RequestParam(name = "page", defaultValue = "0") int page ,
            @RequestParam(name = "size", defaultValue = "10") int size

    ) {
        return ApiResponse.<VoicePageResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh sách voice thành công")
                .data(voicePostService.getByPage(page, size))
                .build();
    }

}
