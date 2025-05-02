package com.tools.seoultech.timoproject.memberAccount.domain;

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
public class MemberAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_account_id")
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

    @OneToOne(mappedBy = "memberAccount", cascade = CascadeType.ALL)
    private RankingInfo rankingInfo;

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateRiotAccount(String puuid, String name, String tag) {
        this.riotAccount = new RiotAccount(puuid, name, tag);
    }

    public void updateUnivAccount(String univName, String univCertifiedEmail) {
        this.certifiedUnivInfo = new CertifiedUnivInfo(univCertifiedEmail, univName);
    }
}
