package com.tools.seoultech.timoproject.member.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SocialAccount {

    /**
     * 회원 식별자 : Provider + " " + ProviderId
     * 예시 : naver JfgjWA4OYe42vQbKCnMNxhGATz0hxg40JQfIUWLzaw4
     */

    @Id
    @Column(name = "social_account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String provider;

    private String providerId;

    @Builder
    public SocialAccount(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    public void linkMember(Member member) {
        this.member = member;
    }
}

