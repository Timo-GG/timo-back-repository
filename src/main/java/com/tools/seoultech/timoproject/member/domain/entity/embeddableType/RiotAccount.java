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

    /**
     * 기존 RiotAccount의 정보를 유지하면서 인증 타입만 업데이트한 새로운 인스턴스 생성
     * 인증 타입이 변경될 때 verifiedAt도 현재 시간으로 업데이트
     */
    public static RiotAccount withUpdatedVerificationType(RiotAccount original, RiotVerificationType newVerificationType) {
        if (original == null) {
            throw new IllegalArgumentException("원본 RiotAccount가 null입니다.");
        }

        if (newVerificationType == null) {
            throw new IllegalArgumentException("새로운 인증 타입이 null입니다.");
        }

        // 기존 정보 유지하면서 새로운 인증 타입과 현재 시간으로 업데이트
        RiotAccount updated = new RiotAccount(
                original.getPuuid(),
                original.getGameName(),
                original.getTagLine(),
                original.getProfileUrl(),
                newVerificationType
        );

        return updated;
    }

    /**
     * verifiedAt을 명시적으로 설정할 수 있는 정적 팩토리 메서드
     * 주로 테스트나 특별한 케이스에서 사용
     */
    public static RiotAccount withUpdatedVerificationTypeAndTime(RiotAccount original,
                                                                 RiotVerificationType newVerificationType,
                                                                 LocalDateTime verifiedAt) {
        if (original == null) {
            throw new IllegalArgumentException("원본 RiotAccount가 null입니다.");
        }

        RiotAccount updated = new RiotAccount(
                original.getPuuid(),
                original.getGameName(),
                original.getTagLine(),
                original.getProfileUrl(),
                newVerificationType
        );

        // verifiedAt을 수동으로 설정 (리플렉션 대신 생성자에서 처리)
        updated.verifiedAt = verifiedAt;

        return updated;
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