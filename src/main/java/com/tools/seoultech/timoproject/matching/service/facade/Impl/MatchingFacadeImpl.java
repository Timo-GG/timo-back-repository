package com.tools.seoultech.timoproject.matching.service.facade.Impl;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.service.MatchingService;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.matching.service.facade.MatchingFacade;
import com.tools.seoultech.timoproject.matching.service.facade.MyPageFacade;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchingFacadeImpl implements MatchingFacade {
    private final MatchingService matchingService;
    private final MyPageService myPageService;
    private final MyPageFacade myPageFacade;
    private final MyPageMapper myPageMapper;

    @Override
    public MyPageDTO.Response doAcceptEvent(UUID myPageUUID) throws Exception {
            MatchingDTO.Response dto = myPageFacade.readMyPage(myPageUUID);
            if(dto instanceof MatchingDTO.ResponseDuo duoDto ){
                DuoPage entity = matchingService.doDuoAcceptEvent(myPageUUID);
                return myPageMapper.toDuoDto(entity);

            } else if (dto instanceof MatchingDTO.ResponseScrim scrimDto ){
                ScrimPage entity = matchingService.doScrimAcceptEvent(myPageUUID);
                return myPageMapper.toScrimDto(entity);
            }
            throw new GeneralException("Matching 로직 내부에서 실패했습니다.");
    }

    @Override
    public void doRejectEvent(UUID myPageUUID) throws Exception {
        MatchingDTO.Response dto = myPageFacade.readMyPage(myPageUUID);
        if(dto instanceof MatchingDTO.ResponseDuo duoDto ){
            matchingService.doDuoRejectEvent(myPageUUID);
            return;
        } else if (dto instanceof MatchingDTO.ResponseScrim scrimDto ){
            matchingService.doScrimRejectEvent(myPageUUID);
            return;
        }
        throw new GeneralException("Matching 로직 내부에서 실패했습니다.");
    }
}
