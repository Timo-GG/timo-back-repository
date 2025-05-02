package com.tools.seoultech.timoproject.version2.memberAccount.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private CertifiedUnivInfo certifiedUnivInfo;

}
