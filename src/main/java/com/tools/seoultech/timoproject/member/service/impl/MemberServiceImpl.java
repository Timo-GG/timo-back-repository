    package com.tools.seoultech.timoproject.member.service.impl;

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
    }
