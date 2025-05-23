package com.tools.seoultech.timoproject.member.domain;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.match.domain.DuoInfo;
import com.tools.seoultech.timoproject.match.domain.UserInfo;
import com.tools.seoultech.timoproject.post.domain.entity.Comment;
import com.tools.seoultech.timoproject.post.domain.entity.Post;
import com.tools.seoultech.timoproject.rating.Rating;
import jakarta.persistence.*;
import lombok.*;

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

    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String playerName;

    private String playerTag;

    private Integer profileImageId = 1;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_info_id")
    private UserInfo userInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "duo_info_id")
    private DuoInfo duoInfo;

    public void updateMatchOption(UserInfo newUserInfo, DuoInfo newDuoInfo) {
        if (this.userInfo == null) this.userInfo = newUserInfo;
        else {
            this.userInfo.update(
                    newUserInfo.getIntroduce(),
                    newUserInfo.getGameMode(),
                    newUserInfo.getPlayPosition(),
                    newUserInfo.getPlayCondition(),
                    newUserInfo.getVoiceChat(),
                    newUserInfo.getPlayStyle()
            );
        }
        if (this.duoInfo == null) this.duoInfo = newDuoInfo;
        else {
            this.duoInfo.update(
                    newDuoInfo.getDuoPlayPosition(),
                    newDuoInfo.getDuoPlayStyle()
            );
        }
    }

    @Builder
    public Member(String email, String nickname, Integer profileImageId, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
        this.oAuthProvider = oAuthProvider;
        this.role = Role.MEMBER;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

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
        this.nickname = "닉네임없음";
        this.playerName = "정보없음";
        this.playerTag = "정보없음";
        this.email = "anonymous_" + UUID.randomUUID().toString() + "@anonymous.com";
    }

    public void updateProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}