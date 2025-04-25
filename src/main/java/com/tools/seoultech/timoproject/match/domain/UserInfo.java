package com.tools.seoultech.timoproject.match.domain;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayCondition;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayStyle;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.VoiceChat;
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

    public void update(String introduce, GameMode gameMode, PlayPosition playPosition,
                       PlayCondition playCondition, VoiceChat voiceChat, PlayStyle playStyle) {
        this.introduce = introduce;
        this.gameMode = gameMode;
        this.playPosition = playPosition;
        this.playCondition = playCondition;
        this.voiceChat = voiceChat;
        this.playStyle = playStyle;
    }
}