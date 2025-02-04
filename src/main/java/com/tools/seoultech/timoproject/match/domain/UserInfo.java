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
public class UserInfo {

    /**
     * TODO : 다중 선택 옵션 결정 필요
     */

    @Id
    @Column(name = "user_info_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String introduce;

    @Enumerated(EnumType.STRING)
    private GameMode gameMode;

    @Enumerated(EnumType.STRING)
    private PlayPosition playPosition;

    @Enumerated(EnumType.STRING)
    private PlayCondition playCondition;

    @Enumerated(EnumType.STRING)
    private VoiceChat voiceChat;

    @Enumerated(EnumType.STRING)
    private PlayStyle playStyle;

    // TODO : 양방향 관계 필요한지 논의
    @OneToOne(mappedBy = "userInfo")
    private Member member;

    @Builder
    public UserInfo(String introduce, GameMode gameMode, PlayPosition playPosition,
                    PlayCondition playCondition, VoiceChat voiceChat, PlayStyle playStyle) {
        this.introduce = introduce;
        this.gameMode = gameMode;
        this.playPosition = playPosition;
        this.playCondition = playCondition;
        this.voiceChat = voiceChat;
        this.playStyle = playStyle;
    }
}