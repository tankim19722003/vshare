package com.vshare.vshare.feature.voice.boundary.dto.request;

import com.vshare.vshare.feature.voice.boundary.dto.request.validator.IsAudio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VoicePostDto {

    private String title;

    @IsAudio
    private MultipartFile audio;
}
