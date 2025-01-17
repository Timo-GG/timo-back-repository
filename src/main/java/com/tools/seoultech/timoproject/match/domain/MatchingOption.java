package com.tools.seoultech.timoproject.match.domain;

import com.tools.seoultech.timoproject.match.dto.MatchingOptionRequest;
import com.tools.seoultech.timoproject.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchingOption {

    @Id
    @Column(name = "match_option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String introduce; // 한줄 소개

    @Enumerated(value = EnumType.STRING)
    private Age age; // 연령대

    @Enumerated(value = EnumType.STRING)
    private Gender gender; // 성별

    @Enumerated(value = EnumType.STRING)
    private VoiceChat voiceChat; // 디스코드 여부

    @Enumerated(value = EnumType.STRING)
    private PlayStyle playStyle; // 게임 플레이 스타일

    @Enumerated(value = EnumType.STRING)
    private PlayTime playTime; // 플레이 시간대

    @Enumerated(value = EnumType.STRING)
    private GameMode gameMode; // 게임모드(일반, 랭크)

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void linkMember(Member member) {
        this.member = member;
    }

    @Builder
    public MatchingOption(String introduce, Age age, Gender gender, VoiceChat voiceChat,
                          PlayStyle playStyle, PlayTime playTime, GameMode gameMode, Member member) {
        this.introduce = introduce;
        this.age = age;
        this.gender = gender;
        this.voiceChat = voiceChat;
        this.playStyle = playStyle;
        this.playTime = playTime;
        this.gameMode = gameMode;
        this.member = member;
    }

    public void update(MatchingOptionRequest requestDto) {
        this.introduce = requestDto.getIntroduce();
        this.age = requestDto.getAge();
        this.gender = requestDto.getGender();
        this.voiceChat = requestDto.getVoiceChat();
        this.playStyle = requestDto.getPlayStyle();
        this.playTime = requestDto.getPlayTime();
        this.gameMode = requestDto.getGameMode();
    }
}
