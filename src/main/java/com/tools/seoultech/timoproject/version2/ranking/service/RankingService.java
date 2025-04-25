package com.tools.seoultech.timoproject.version2.ranking.service;

import com.tools.seoultech.timoproject.global.constant.ErrorCode;
import com.tools.seoultech.timoproject.global.exception.BusinessException;
import com.tools.seoultech.timoproject.version2.ranking.RankingInfo;
import com.tools.seoultech.timoproject.version2.ranking.dto.RankingUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingService {
    private final RankingInfoRepository rankingInfoRepository;

    @Transactional
    public void updateRankingInfo(Long memberId, RankingUpdateRequestDto dto) {
        RankingInfo entity = rankingInfoRepository.findByMemberAccountMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        entity.updateFrom(dto);

        rankingInfoRepository.save(entity);
    }
}