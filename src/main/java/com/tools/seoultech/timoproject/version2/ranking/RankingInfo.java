package com.tools.seoultech.timoproject.version2.ranking;


import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.Gender;
import com.tools.seoultech.timoproject.version2.matching.user.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
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
    @JoinColumn(name = "member_account_id")
    private MemberAccount memberAccount;


}
