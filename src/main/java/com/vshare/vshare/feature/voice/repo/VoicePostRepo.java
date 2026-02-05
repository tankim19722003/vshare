package com.vshare.vshare.feature.voice.repo;

import com.vshare.vshare.feature.voice.entity.VoicePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoicePostRepo extends JpaRepository<VoicePost, String> {
}
