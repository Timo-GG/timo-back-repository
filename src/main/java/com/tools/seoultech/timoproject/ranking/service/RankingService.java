package com.tools.seoultech.timoproject.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import com.tools.seoultech.timoproject.ranking.dto.RankingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService {
    private final RankingInfoRepository rankingInfoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        RankingInfo entity = rankingInfoRepository.findByMemberMemberId(memberId)
                .orElseGet(() -> {
                    RankingInfo newEntity = RankingInfo.builder()
                            .member(memberRepository.findById(memberId)
                                    .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)))
                            .build();
                    return rankingInfoRepository.save(newEntity);
                });

        entity.updateFrom(dto);

        if (dto.department() != null &&
                !dto.department().trim().isEmpty() &&
                entity.getMember().getCertifiedUnivInfo() != null) {
            entity.getMember()
                    .getCertifiedUnivInfo()
                    .updateDepartment(dto.department());
        }

        rankingInfoRepository.save(entity);
    }
}