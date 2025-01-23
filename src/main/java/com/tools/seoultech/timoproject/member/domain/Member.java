package com.tools.seoultech.timoproject.member.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.match.domain.MatchingOption;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String username;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String playerName;

    private String playerTag;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MatchingOption matchingOption;

    public void linkMatchingOption(MatchingOption matchingOption) {
        this.matchingOption = matchingOption;
        matchingOption.linkMember(this);
    }

    @Builder
    public Member(String email, String username) {
        this.email = email;
        this.username = username;
        this.role = Role.MEMBER;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    public void linkSocialAccount(SocialAccount socialAccount) {
        this.socialAccounts.add(socialAccount);
        socialAccount.linkMember(this);
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    // TODO : 회원 가입 이후 유저의 소환사 정보 기입하도록...
    public void linkRiotInfo(String playerName, String playerTag) {
        this.playerName = playerName;
        this.playerTag = playerTag;
    }
}