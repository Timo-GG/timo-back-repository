package com.tools.seoultech.timoproject.member.domain.entity;

import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private CertifiedUnivInfo certifiedUnivInfo;

    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private RankingInfo rankingInfo;

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateRiotAccount(String puuid, String name, String tag, String profileUrl) {
        this.riotAccount = new RiotAccount(puuid, name, tag, profileUrl);
    }

    public void updateUnivAccount(String univName, String univCertifiedEmail) {
        this.certifiedUnivInfo = new CertifiedUnivInfo(univCertifiedEmail, univName);
    }
}
