package com.vshare.vshare.feature.voice.entity;

import com.vshare.vshare.feature.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "voice_posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class VoicePost {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "total_like")
    private int totalLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    public void onPrePersist() {
        this.totalLike = 0;
        createdAt = LocalDateTime.now();
    }
}
