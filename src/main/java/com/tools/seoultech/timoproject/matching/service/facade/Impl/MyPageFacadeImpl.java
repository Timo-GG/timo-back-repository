package com.tools.seoultech.timoproject.matching.service.facade.Impl;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.ScrimMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.DuoMyPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.MyPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.ScrimMyPageOnly;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.matching.service.facade.MyPageFacade;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class MyPageFacadeImpl implements MyPageFacade {
    private final MyPageService myPageService;
    private final MyPageMapper myPageMapper;

    @Override
    public MatchingDTO.Response createMyPage(MatchingDTO.Request dto) throws Exception {
        if(dto instanceof MatchingDTO.RequestDuo duoDto) {
            DuoMyPage entity = myPageService.createDuoMyPage(duoDto);
            return myPageMapper.toDuoDto(entity);
        } else if(dto instanceof MatchingDTO.RequestScrim scrimDto) {
            ScrimMyPage entity = myPageService.createScrimMyPage(scrimDto);
            return myPageMapper.toScrimDto(entity);
        }
        throw new GeneralException("마이페이지 요청 dto 데이터 형식이 맞지 않습니다.");
    }

    @Override
    public MatchingDTO.Response readMyPage(UUID myPageUUID) throws Exception {
        MyPageOnly proj = myPageService.getBoard(myPageUUID);
        if(proj instanceof DuoMyPageOnly){
            return myPageMapper.toDuoDto((DuoMyPageOnly) proj);
        } else if (proj instanceof ScrimMyPageOnly){
            return myPageMapper.toScrimDto((ScrimMyPageOnly) proj);
        }
        throw new GeneralException("해당 UUID는 Duo, Scrim 레디스 저장소에 없습니다.");
    }

    @Override
    public List<MatchingDTO.Response> readAllMyPage(MatchingCategory matchingCategory) throws Exception {
        List<MatchingDTO.Response> dtoList = new ArrayList<>();
        if(matchingCategory == MatchingCategory.DUO){
            dtoList = myPageService.getAllDuoMyPage().stream()
                    .map(proj -> (MatchingDTO.Response) myPageMapper.toDuoDto(proj)).toList();
        } else if(matchingCategory == MatchingCategory.SCRIM){
            dtoList = myPageService.getAllScrimMyPage().stream()
                    .map(proj -> (MatchingDTO.Response) myPageMapper.toScrimDto(proj)).toList();
        }
        return dtoList;
    }

    @Override
    public void deleteMyPage(UUID myPageUUID) throws Exception {
        myPageService.deleteDuoMyPage(myPageUUID);
        myPageService.deleteScrimMyPage(myPageUUID);
    }

    @Override
    public void deleteAllMyPage(MatchingCategory matchingCategory) throws Exception {
        if(matchingCategory == MatchingCategory.DUO){
            myPageService.deleteAllDuoMyPage();
        } else if(matchingCategory == MatchingCategory.SCRIM){
            myPageService.deleteAllScrimMyPage();
        }
    }

    @Override
    public MyPageDTO.Response readMyPage(Long mypageId) throws Exception {
        return null;
    }

    @Override
    public List<MyPageDTO.ResponseMyPage> readMyPageByMemberId(Long MemberId) throws Exception {
        return List.of();
    }
}
