    package com.tools.seoultech.timoproject.member.service.impl;

    import com.tools.seoultech.timoproject.auth.dto.OAuthInfoResponse;
    import com.tools.seoultech.timoproject.auth.dto.RiotInfoResponse;
    import com.tools.seoultech.timoproject.auth.dto.RiotLoginParams;
    import com.tools.seoultech.timoproject.auth.service.RequestOAuthInfoService;
    import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
    import com.tools.seoultech.timoproject.global.constant.ErrorCode;
    import com.tools.seoultech.timoproject.global.exception.BusinessException;
    import com.tools.seoultech.timoproject.member.domain.entity.Member;
    import com.tools.seoultech.timoproject.member.domain.entity.enumType.RiotVerificationType;
    import com.tools.seoultech.timoproject.member.domain.entity.enumType.UserAgreement;
    import com.tools.seoultech.timoproject.member.dto.NotificationEmailResponse;
    import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
    import com.tools.seoultech.timoproject.member.service.MemberService;
    import com.tools.seoultech.timoproject.member.MemberRepository;
    import jakarta.persistence.EntityManager;
    import jakarta.persistence.EntityNotFoundException;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Propagation;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.UUID;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class MemberServiceImpl implements MemberService {
        private final MemberRepository memberRepository;
        private final EntityManager entityManager;
        private final RequestOAuthInfoService requestOAuthInfoService;


        @Override
        public Member getById(Long memberId) {
            return memberRepository.findById(memberId).
                    orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        }

        public boolean checkUsername(String username) {
            return memberRepository.existsByUsername(username);
        }

        @Override
        public String randomCreateUsername() {
            return "티모대위" + "-" + UUID.randomUUID().toString().substring(0, 5);
        }

        @Override
        public Integer randomCreateProfileImageId() {
            // 1~6 범위의 랜덤 숫자 생성
            return (int) (Math.random() * 6) + 1;
        }

        @Transactional
        @Override
        public Member updateAccountInfo(Long memberId, UpdateMemberInfoRequest request) {
            Member member = getById(memberId);

            // 1) 닉네임 중복 체크
            if (checkUsername(request.username())) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            // 2) 엔티티 업데이트
            member.updateUsername(request.username());
            member.updateRiotAccount(request.puuid(), request.playerName(), request.playerTag(), request.profileIconUrl());
            member.updateUnivAccount(request.univName(), request.univEmail());

            // 3) 저장 (영속성 컨텍스트에서 변경 감지로 자동 반영)
            return member;
        }

        @Override
        @Transactional
        public Member updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag, String profileIconUrl) {
            Member member = getById(memberId);
            // 중복된 소환사 puuid 존재 여부 체크
//            if (puuid != null) {
//                // RSO_VERIFIED 계정의 PUUID가 이미 사용 중인지 확인
//                boolean isRSOPuuidUsed = memberRepository.existsByRiotAccount_PuuidAndRiotAccount_VerificationTypeAndMemberIdNot(
//                        puuid, RiotVerificationType.RSO_VERIFIED, memberId);
//
//                if (isRSOPuuidUsed) {
//                    throw new BusinessException(ErrorCode.ALREADY_USED_RIOT_ACCOUNT);
//                }
//            }

            member.updateRiotAccount(puuid, playerName, playerTag, profileIconUrl);

            return member;
        }

        @Transactional
        @Override
        public Member updateUsername(Long memberId, String username) {
            Member member = getById(memberId);
            if(checkUsername(username)) {
                throw new BusinessException(ErrorCode.ALREADY_USED_USERNAME);
            }
            member.updateUsername(username);

            return member;
        }

        @Transactional
        @Override
        public Member updateUniv(Long memberId, UnivRequestDTO univ) {
            Member member = getById(memberId);

            if (univ.univEmail() != null && memberRepository.existsByCertifiedUnivInfo_UnivCertifiedEmail(univ.univEmail())) {
                throw new BusinessException(ErrorCode.ALREADY_USED_UNIV_ACCOUNT);
            }

            member.updateUnivAccount(univ.univName(), univ.univEmail());

            return member;
        }

        @Transactional
        @Override
        public void updateUserAgreement(Long memberId) {
            Member member = getById(memberId);

            if(member.getTerm() == UserAgreement.DISABLED || member.getTerm() == UserAgreement.ENABLED) {
                throw new BusinessException(ErrorCode.ALREADY_AGREE_AGREEMENT);
            }
            member.updateUserAgreement(UserAgreement.ENABLED);
            entityManager.flush();
        }

        @Transactional(propagation = Propagation.REQUIRES_NEW)
        @Override
        public void softDeleteUserAgreement(Long memberId) {
            Member member = getById(memberId);
            if(member.getTerm() == UserAgreement.DISABLED) {
                throw new BusinessException(ErrorCode.ALREADY_DISABLED_AGREEMENT);
            }
            member.updateUserAgreement(UserAgreement.REMOVABLE);

            memberRepository.saveAndFlush(member); // 저장 + 플러시 한번에
        }

        @Transactional
        @Override
        public void hardDeleteUserAgreement(Long memberId) {
            Member member = getById(memberId);
            if(member.getTerm() != UserAgreement.REMOVABLE) {
                throw new BusinessException(ErrorCode.NOT_REMOVABLE_AGREEMENT);
            }
            memberRepository.deleteById(memberId);
        }

        @Override
        @Transactional
        public String linkRiotAccount(Long memberId, RiotLoginParams params) {
            Member member = getById(memberId);

            // 이미 RSO 인증된 계정인지 확인
            if (member.getRiotAccount() != null &&
                    member.getRiotAccount().getVerificationType() == RiotVerificationType.RSO_VERIFIED) {
                throw new BusinessException(ErrorCode.ALREADY_LINKED_RIOT_ACCOUNT);
            }

            // 🔥 기존 RequestOAuthInfoService 활용
            OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
            RiotInfoResponse riotInfo = (RiotInfoResponse) oAuthInfoResponse;

            // RSO 계정 정보로 업데이트
            if (riotInfo.getPuuid() != null && riotInfo.getGameName() != null) {
                // 🔥 기존 updateRiotAccount 메서드 확장
                updateRiotAccountWithRSO(member, riotInfo);

                log.info("✅ 기존 회원 Riot 계정 연동 완료: {}#{}",
                        riotInfo.getGameName(), riotInfo.getTagLine());

                return "Riot 계정 연동이 완료되었습니다";
            } else {
                throw new BusinessException(ErrorCode.RIOT_ACCOUNT_INFO_NOT_FOUND);
            }
        }

        @Transactional
        @Override
        public void updateNotificationEmail(Long memberId, String notificationEmail) {
            Member member = getById(memberId);

            validateNotificationEmail(notificationEmail);
            member.updateNotificationEmail(notificationEmail);
            memberRepository.save(member);

            log.info("알림 이메일 설정 변경 - memberId: {}, email: {}", memberId, notificationEmail);
        }

        @Transactional(readOnly = true)
        @Override
        public NotificationEmailResponse getNotificationEmailSettings(Long memberId) {
            Member member = getById(memberId);
            return NotificationEmailResponse.from(member);
        }

        @Transactional
        @Override
        public Member save(Member member) {
            return memberRepository.save(member);
        }

        private void validateNotificationEmail(String email) {
            if (email != null && !email.trim().isEmpty()) {
                if (!isValidEmail(email)) {
                    throw new BusinessException(ErrorCode.INVALID_EMAIL_FORMAT);
                }
            }
        }

        private boolean isValidEmail(String email) {
            return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
        }

        private void updateRiotAccountWithRSO(Member member, RiotInfoResponse riotInfo) {
            // 중복된 소환사 puuid 존재 여부 체크
            if (memberRepository.existsByRiotAccount_PuuidAndMemberIdNot(
                    riotInfo.getPuuid(), member.getMemberId())) {
                throw new BusinessException(ErrorCode.ALREADY_USED_RIOT_ACCOUNT);
            }

            // RSO 인증으로 업데이트
            member.updateRiotAccountWithRSO(
                    riotInfo.getPuuid(),
                    riotInfo.getGameName(),
                    riotInfo.getTagLine(),
                    riotInfo.getProfileUrl()
            );
        }
    }
