package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import com.tools.seoultech.timoproject.member.domain.Role;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_account_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private CertifiedUnivInfo certifiedUnivInfo;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    public void updateUsername(String username) {
        this.userName = username;
    }

    public void updateRiotAccount(String puuid, String name, String tag) {
        this.riotAccount = new RiotAccount(puuid, name, tag);
    }

    public void updateUnivAccount(String univName, String univCertifiedEmail) {
        this.certifiedUnivInfo = new CertifiedUnivInfo(univCertifiedEmail, univName);
    }
}
