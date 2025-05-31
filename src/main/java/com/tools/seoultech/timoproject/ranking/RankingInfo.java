package com.tools.seoultech.timoproject.ranking;


import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankingInfo {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long rankId;

    @Column(name = "mbti")
    private String mbti = "UNKNOWN";

    @Enumerated(value = EnumType.STRING)
    private PlayPosition position = PlayPosition.NOTHING;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender = Gender.SECRET;

    private String memo;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void updateFrom(RankingUpdateRequestDto dto) {
        if (dto.mbti() != null) this.mbti = dto.mbti();
        if (dto.memo() != null) this.memo = dto.memo();
        if (dto.position() != null) this.position = dto.position();
        if (dto.gender() != null) this.gender = dto.gender();
    }

}
