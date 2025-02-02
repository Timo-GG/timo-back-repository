package com.tools.seoultech.timoproject.match.domain;

import com.tools.seoultech.timoproject.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DuoInfo {

    /**
     * TODO : 다중 선택 옵션 결정 필요
     */

    @Id
    @Column(name = "duo_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlayPosition duoPlayPosition;

    @Enumerated(EnumType.STRING)
    private PlayTime duoPlayTime;

    @Enumerated(EnumType.STRING)
    private VoiceChat duoVoiceChat;

    @Enumerated(EnumType.STRING)
    private Age duoAge;

    // TODO : 양방향 관계 필요한지 논의
    @OneToOne(mappedBy = "duoInfo")
    private Member member;

    @Builder
    public DuoInfo(PlayPosition duoPlayPosition, PlayTime duoPlayTime, VoiceChat duoVoiceChat, Age duoAge) {
        this.duoPlayPosition = duoPlayPosition;
        this.duoPlayTime = duoPlayTime;
        this.duoVoiceChat = duoVoiceChat;
        this.duoAge = duoAge;
    }
}