package com.tools.seoultech.timoproject.member.dto;

import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.UserAgreement;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;

public record MemberDto(
        Long memberId,
        String email,
        String username,
        RiotAccountDto riotAccount,
        CertifiedUnivInfoDto certifiedUnivInfo,
        Role role,
        RankingInfoDto rankingInfo,
        UserAgreement term,
        String notificationEmail
) {
    public static MemberDto from(Member entity) {
        return new MemberDto(
                entity.getMemberId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getRiotAccount() != null ? RiotAccountDto.from(entity.getRiotAccount()) : null,
                entity.getCertifiedUnivInfo() != null ? CertifiedUnivInfoDto.from(entity.getCertifiedUnivInfo()) : null,
                entity.getRole(),
                entity.getRankingInfo() != null ? RankingInfoDto.from(entity.getRankingInfo()) : null,
                entity.getTerm(),
                entity.getNotificationEmail()
        );
    }

    public record RiotAccountDto(
            String puuid,
            String accountName,
            String accountTag,
            String profileUrl,
            RiotVerificationType verificationType
    ) {
        public static RiotAccountDto from(RiotAccount entity) {
            return new RiotAccountDto(
                    entity.getPuuid(),
                    entity.getGameName(),
                    entity.getTagLine(),
                    entity.getProfileUrl(),
                    entity.getVerificationType()
            );
        }
    }

    public record CertifiedUnivInfoDto(
            String univName,
            String univCertifiedEmail,
            String department
    ) {
        public static CertifiedUnivInfoDto from(CertifiedUnivInfo entity) {
            return new CertifiedUnivInfoDto(
                    entity.getUnivName(),
                    entity.getUnivCertifiedEmail(),
                    entity.getDepartment()
            );
        }
    }

    public record RankingInfoDto(
            String mbti,
            PlayPosition position,
            Gender gender,
            String memo
    ) {
        public static RankingInfoDto from(RankingInfo entity) {
            return new RankingInfoDto(
                    entity.getMbti(),
                    entity.getPosition(),
                    entity.getGender(),
                    entity.getMemo()
            );
        }
    }
}
