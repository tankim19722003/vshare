package com.vshare.vshare.feature.voice.boundary.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoiceResponse {
    private String id;

    private String title;

    private LocalDateTime createdAt;

    private int totalLike;

    private String voiceUrl;

}
