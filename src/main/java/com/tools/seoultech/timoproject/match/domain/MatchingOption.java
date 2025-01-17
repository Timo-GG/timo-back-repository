package com.tools.seoultech.timoproject.match.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

}
