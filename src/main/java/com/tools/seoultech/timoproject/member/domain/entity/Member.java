package com.tools.seoultech.timoproject.member.domain.entity;

import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.TermsOfService;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.UserAgreement;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
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

    @Column(name = "notification_email")
    @Email
    private String notificationEmail;

    @Embedded
    private RiotAccount riotAccount;

    @Embedded
    private CertifiedUnivInfo certifiedUnivInfo;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "o_auth_provider", length = 20)
    private OAuthProvider oAuthProvider;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private RankingInfo rankingInfo;

    @Enumerated(value = EnumType.STRING)
    private UserAgreement term = UserAgreement.NOTHING;

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateRiotAccount(String puuid, String name, String tag, String profileUrl) {
        if(puuid != null){
            this.riotAccount = new RiotAccount(puuid, name, tag, profileUrl, RiotVerificationType.API_PARSED);
        }else{
            this.riotAccount = new RiotAccount(puuid, name, tag, profileUrl, RiotVerificationType.NONE);
        }
    }

    public void updateUnivAccount(String univName, String univCertifiedEmail) {
        this.certifiedUnivInfo = new CertifiedUnivInfo(univCertifiedEmail, univName);
    }

    public void updateUserAgreement(UserAgreement term) {
        this.term = term;
    }

    public void updateRiotAccountWithRSO(String puuid, String name, String tag, String profileUrl) {
        this.riotAccount = new RiotAccount(puuid, name, tag, profileUrl,
                RiotVerificationType.RSO_VERIFIED);
    }

    /**
     * RiotAccount의 인증 타입만 업데이트
     * 기존 RiotAccount 정보는 유지하면서 verificationType만 변경
     */
    public void updateRiotAccountVerificationType(RiotVerificationType newVerificationType) {
        if (this.riotAccount == null) {
            throw new IllegalStateException("RiotAccount가 존재하지 않습니다. 인증 타입을 업데이트할 수 없습니다.");
        }

        this.riotAccount = RiotAccount.withUpdatedVerificationType(this.riotAccount, newVerificationType);
    }

    public void updateNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public boolean shouldReceiveEmailNotification() {
        return notificationEmail != null && !notificationEmail.trim().isEmpty();
    }

    public String getEmailForNotification() {
        return shouldReceiveEmailNotification() ? notificationEmail : null;
    }

    public void updateCertifiedUnivInfo(CertifiedUnivInfo certifiedUnivInfo) {
        this.certifiedUnivInfo = certifiedUnivInfo;
    }

    public void clearCertifiedUnivInfo() {
        this.certifiedUnivInfo = new CertifiedUnivInfo();
    }
}