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

    @Enumerated(EnumType.STRING)
    private Age age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private VoiceChat voiceChat;



}
