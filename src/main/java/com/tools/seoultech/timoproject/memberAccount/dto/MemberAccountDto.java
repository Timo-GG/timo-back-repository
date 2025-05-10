    package com.tools.seoultech.timoproject.memberAccount.dto;

    import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.CertifiedUnivInfo;
    import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
    import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
    import com.tools.seoultech.timoproject.memberAccount.domain.entity.enumType.Role;

    public record MemberAccountDto(
            Long memberId,
            String email,
            String username,
            RiotAccountDto riotAccount,
            CertifiedUnivInfoDto certifiedUnivInfo,
            Role role
    ) {
        public static MemberAccountDto from(MemberAccount entity) {
            return new MemberAccountDto(
                    entity.getMemberId(),
                    entity.getEmail(),
                    entity.getUsername(),
                    entity.getRiotAccount() != null ? RiotAccountDto.from(entity.getRiotAccount()) : null,
                    entity.getCertifiedUnivInfo() != null ? CertifiedUnivInfoDto.from(entity.getCertifiedUnivInfo()) : null,
                    entity.getRole()
            );
        }

        public record RiotAccountDto(
                String puuid,
                String accountName,
                String accountTag,
                String profileUrl
        ) {
            public static RiotAccountDto from(RiotAccount entity) {
                return new RiotAccountDto(
                        entity.getPuuid(),
                        entity.getAccountName(),
                        entity.getAccountTag(),
                        entity.getProfileUrl()
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
    }
