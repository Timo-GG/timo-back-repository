    package com.tools.seoultech.timoproject.member.service.impl;

    import com.tools.seoultech.timoproject.auth.univ.UnivRequestDTO;
    import com.tools.seoultech.timoproject.global.constant.ErrorCode;
    import com.tools.seoultech.timoproject.global.exception.BusinessException;
    import com.tools.seoultech.timoproject.member.domain.Member;
    import com.tools.seoultech.timoproject.member.dto.UpdateMemberInfoRequest;
    import com.tools.seoultech.timoproject.member.repository.MemberRepository;
    import com.tools.seoultech.timoproject.member.service.MemberService;
    import com.tools.seoultech.timoproject.version2.memberAccount.MemberAccountRepository;
    import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
    import jakarta.persistence.EntityNotFoundException;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.UUID;

    @Service
    @RequiredArgsConstructor
    public class MemberServiceImpl implements MemberService {

        private final MemberAccountRepository memberAccountRepository;

        @Override
        public MemberAccount getById(Long memberId) {
            return memberAccountRepository.findById(memberId).
                    orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
        }

        public boolean checkUsername(String username) {
            return memberAccountRepository.existsByUsername(username);
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
        public MemberAccount updateAccountInfo(Long memberId, UpdateMemberInfoRequest request) {
            MemberAccount member = getById(memberId);

            // 1) 닉네임 중복 체크
            if (checkUsername(request.username())) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            // 2) 엔티티 업데이트
            member.updateUsername(request.username());
            member.updateRiotAccount(request.puuid(), request.playerName(), request.playerTag());
            member.updateUnivAccount(request.univName(), request.univEmail());

            // 3) 저장 (영속성 컨텍스트에서 변경 감지로 자동 반영)
            return member;
        }

        @Override
        @Transactional
        public MemberAccount updateRiotAccount(Long memberId, String puuid, String playerName, String playerTag) {
            MemberAccount member = getById(memberId);
            // 중복된 소환사 puuid 존재 여부 체크
            if (puuid != null && memberAccountRepository.existsByRiotAccount_PuuidAndMemberIdNot(puuid, memberId)) {
                throw new BusinessException(ErrorCode.ALREADY_USED_RIOT_ACCOUNT);
            }
            member.updateRiotAccount(puuid, playerName, playerTag);

            return member;
        }

        @Transactional
        @Override
        public MemberAccount updateUsername(Long memberId, String username) {
            MemberAccount member = getById(memberId);
            if(checkUsername(username)) {
                throw new BusinessException(ErrorCode.ALREADY_USED_USERNAME);
            }
            member.updateUsername(username);

            return member;
        }

        @Transactional
        @Override
        public MemberAccount updateUniv(Long memberId, UnivRequestDTO univ) {
            MemberAccount member = getById(memberId);

            if (univ.univEmail() != null && memberAccountRepository.existsByCertifiedUnivInfo_UnivCertifiedEmail(univ.univEmail())) {
                throw new BusinessException(ErrorCode.ALREADY_USED_UNIV_ACCOUNT);
            }

            member.updateUnivAccount(univ.univName(), univ.univEmail());

            return member;
        }
    }
