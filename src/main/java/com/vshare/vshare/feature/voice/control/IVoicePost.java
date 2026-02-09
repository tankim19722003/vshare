package com.vshare.vshare.feature.voice.control;

import com.vshare.vshare.feature.voice.boundary.dto.request.VoicePostDto;
import com.vshare.vshare.feature.voice.boundary.dto.response.VoiceResponse;

import java.util.List;

public interface IVoicePost {
    void create(VoicePostDto dto);
    List<VoiceResponse> getByPage(int page, int size);
}
