package com.tools.seoultech.timoproject.member.domain.entity.embeddableType;

import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor
public class RiotAccount {
    private String puuid;
    private String gameName;
    private String tagLine;
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "riot_verification_type")
    private RiotVerificationType verificationType;

    @Column(name = "riot_verified_at")
    private LocalDateTime verifiedAt;

    public RiotAccount(String puuid, String gameName, String tagLine, String profileUrl) {
        this.puuid = puuid;
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.profileUrl = profileUrl;
        this.verificationType = RiotVerificationType.API_PARSED; // 기본값
        this.verifiedAt = LocalDateTime.now();
    }

    // RSO 인증용 생성자
    public RiotAccount(String puuid, String gameName, String tagLine, String profileUrl,
                       RiotVerificationType verificationType) {
        this.puuid = puuid;
        this.gameName = gameName;
        this.tagLine = tagLine;
        this.profileUrl = profileUrl;
        this.verificationType = verificationType;
        this.verifiedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.verificationType == null) {
            this.verificationType = RiotVerificationType.API_PARSED;
        }
        if (this.verifiedAt == null) {
            this.verifiedAt = LocalDateTime.now();
        }
    }
}

