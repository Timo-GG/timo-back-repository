package com.tools.seoultech.timoproject.policy.service.impl;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.member.domain.Member;
import com.tools.seoultech.timoproject.member.repository.MemberRepository;
import com.tools.seoultech.timoproject.policy.domain.entity.Policy;
import com.tools.seoultech.timoproject.policy.repository.PolicyRepository;
import com.tools.seoultech.timoproject.policy.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PolicyServiceImpl implements PolicyService {
    private final PolicyRepository policyRepository;
    private final MemberRepository memberRepository;

    @Override
    public void save(Long memberId) {
        Member member = memberRepository
                .findById(memberId)
                .orElseThrow(() -> new GeneralException("해당 사용자가 존재하지 않습니다."));
        policyRepository.save(Policy.builder()
                .member(member)
                .usingAgreement(true)
                .collectingAgreement(true)
                .build()
        );
    }

    @Override
    public void delete(Long memberId) {
        if(!memberRepository.existsById(memberId))
            throw new GeneralException("해당 사용자가 존재하지 않습니다.");

        policyRepository.deleteByMemberId(memberId);
    }

    @Override
    public boolean isExpired(Long memberId) {
        if(!memberRepository.existsById(memberId))
            throw new GeneralException("해당하는 사용자가 존재하지 않습니다.");
        return policyRepository.existsByMemberIdAndRegDateBefore(memberId, LocalDateTime.now().minusYears(5));
    }
}
