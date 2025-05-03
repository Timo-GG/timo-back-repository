package com.tools.seoultech.timoproject.ranking;


import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
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

    private String mbti;

    @Enumerated(value = EnumType.STRING)
    private PlayPosition position;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String memo;

    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberAccount memberAccount;

    public void updateFrom(RankingUpdateRequestDto dto) {
        if (dto.mbti() != null) this.mbti = dto.mbti();
        if (dto.memo() != null) this.memo = dto.memo();
        if (dto.position() != null) this.position = dto.position();
        if (dto.gender() != null) this.gender = dto.gender();
    }

}
