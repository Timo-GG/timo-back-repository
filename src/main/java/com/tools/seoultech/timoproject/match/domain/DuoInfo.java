package com.tools.seoultech.timoproject.match.domain;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.enumType.PlayStyle;
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
    private PlayStyle duoPlayStyle;

    @Builder
    public DuoInfo(PlayPosition duoPlayPosition, PlayStyle duoPlayStyle) {
        this.duoPlayPosition = duoPlayPosition;
        this.duoPlayStyle = duoPlayStyle;
    }

    public void update(PlayPosition duoPlayPosition, PlayStyle duoPlayStyle) {
        this.duoPlayPosition = duoPlayPosition;
        this.duoPlayStyle = duoPlayStyle;
    }
}