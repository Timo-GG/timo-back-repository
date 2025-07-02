package com.tools.seoultech.timoproject.member.service;


import com.tools.seoultech.timoproject.auth.dto.RiotInfoResponse;
import com.tools.seoultech.timoproject.auth.dto.RiotLoginParams;
import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.dto.NotificationEmailResponse;
import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;

public interface MemberService {

    Member getById(Long memberId);

    boolean checkUsername(String nickname);

    String randomCreateUsername();

    Integer randomCreateProfileImageId();

    Member updateAccountInfo(Long memberId, UpdateMemberInfoRequest request);

    Member updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag, String profileIconUrl);

    Member updateUsername(Long memberId, String username);

    Member updateUniv(Long memberId, UnivRequestDTO univ);

    void updateUserAgreement(Long memberId);
    void softDeleteUserAgreement(Long memberId);
    void hardDeleteUserAgreement(Long memberId);

    String linkRiotAccount(Long memberId, RiotLoginParams params);
    void updateVerificationType(Long memberId, String verificationType);

    void updateNotificationEmail(Long memberId, String notificationEmail);
    NotificationEmailResponse getNotificationEmailSettings(Long memberId);
    Member save(Member member);
}
