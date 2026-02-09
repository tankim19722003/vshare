package com.vshare.vshare.feature.voice.boundary.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VoicePageResponse {
    private List<VoiceResponse> voiceResponses;
    private int totalPage;
}
