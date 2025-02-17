package com.tools.seoultech.timoproject.member.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.rating.Rating;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "duo_info_id")
    private DuoInfo duoInfo;

    public void updateMatchOption(UserInfo userInfo, DuoInfo duoInfo) {
        this.userInfo = userInfo;
        this.duoInfo = duoInfo;
    }

    @Builder
    public Member(String email, String username) {
        this.email = email;
        this.username = username;
        this.role = Role.MEMBER;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<SocialAccount> socialAccounts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

    public void linkSocialAccount(SocialAccount socialAccount) {
        this.socialAccounts.add(socialAccount);
        socialAccount.linkMember(this);
    }

    // TODO : 회원 가입 이후 유저의 소환사 정보 기입하도록...
    public void linkRiotInfo(String playerName, String playerTag) {
        this.playerName = playerName;
        this.playerTag = playerTag;
    }

    public void linkRating(Rating rating) {
        this.ratings.add(rating);
        rating.linkMember(this);
    }

    public void updateToDummy() {
        this.username = "이름없음";
        this.playerName = "정보없음";
        this.playerTag = "정보없음";
        this.email = "anonymous_" + UUID.randomUUID().toString() + "@anonymous.com";
    }
}