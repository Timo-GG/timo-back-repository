package com.tools.seoultech.timoproject.matching.service.facade.Impl;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisDuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.PageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import com.tools.seoultech.timoproject.matching.service.MyPageService;
import com.tools.seoultech.timoproject.matching.service.facade.MyPageFacade;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.notification.enumType.NotificationType;
import com.tools.seoultech.timoproject.notification.service.AsyncNotificationService;
import com.tools.seoultech.timoproject.riot.utils.RiotAccountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class MyPageFacadeImpl implements MyPageFacade {
    private final MyPageService myPageService;
    private final MyPageMapper myPageMapper;
    private final MemberService memberService;
    private final AsyncNotificationService asyncNotificationService;


    @Override
    public MatchingDTO.Response createMyPage(MatchingDTO.Request dto) throws Exception {
        if(dto instanceof MatchingDTO.RequestDuo duoDto) {
            RedisDuoPage entity = myPageService.createDuoMyPage(duoDto);

            Long acceptorId = entity.getAcceptorId();
            String requestorNickname = RiotAccountUtil.extractGameNameFromMemberInfo(entity.getRequestorCertifiedMemberInfo());
            asyncNotificationService.sendMatchingApplyNotificationAsync(acceptorId, requestorNickname, NotificationType.DUO_APPLY);

            return myPageMapper.toDuoDto(entity);
        } else if(dto instanceof MatchingDTO.RequestScrim scrimDto) {
            RedisScrimPage entity = myPageService.createScrimMyPage(scrimDto);

            Long acceptorId = entity.getAcceptorId();
            String requestorNickname = RiotAccountUtil.extractGameNameFromMemberInfo(entity.getRequestorCertifiedMemberInfo());
            asyncNotificationService.sendMatchingApplyNotificationAsync(acceptorId, requestorNickname, NotificationType.SCRIM_APPLY);

            return myPageMapper.toScrimDto(entity);
        }
        throw new GeneralException("마이페이지 요청 dto 데이터 형식이 맞지 않습니다.");
    }

    @Override
    public MatchingDTO.Response readMyPage(UUID myPageUUID) throws Exception {
        PageOnly proj = myPageService.getMyPage(myPageUUID);
        if(proj instanceof RedisDuoPageOnly){
            return myPageMapper.toDuoDto((RedisDuoPageOnly) proj);
        } else if (proj instanceof RedisScrimPageOnly){
            return myPageMapper.toScrimDto((RedisScrimPageOnly) proj);
        }
        throw new GeneralException("해당 UUID는 Duo, Scrim 레디스 저장소에 없습니다.");
    }

    @Override
    public List<MatchingDTO.Response> readAllMyPage(MatchingCategory matchingCategory) throws Exception {
        List<MatchingDTO.Response> dtoList = new ArrayList<>();
        if(matchingCategory == MatchingCategory.DUO){
            dtoList = myPageService.getAllDuoMyPage().stream()
                    .map(proj -> (MatchingDTO.Response) myPageMapper.toDuoDto(proj))
                    .filter(Objects::nonNull)
                    .toList();
        } else if(matchingCategory == MatchingCategory.SCRIM){
            dtoList = myPageService.getAllScrimMyPage().stream()
                    .map(proj -> (MatchingDTO.Response) myPageMapper.toScrimDto(proj)).toList();
        }
        return dtoList;
    }

    /**
     * requestorId로 모든 MyPage 조회하기 -> 임시코드
     */
    @Override
    public List<MatchingDTO.Response> readAllMyRequestor(Long requestorId) throws Exception {
        List<MatchingDTO.Response> dtoList = new ArrayList<>();
        dtoList = myPageService.getAllMyPageByRequestorRaw(requestorId).stream()
                .map(this::mapPageOnlyToDto)
                .filter(Objects::nonNull)
                .toList();
        return dtoList;
    }

    /**
     * acceptorId로 모든 MyPage 조회하기 -> 임시코드
     */
    @Override
    public List<MatchingDTO.Response> readAllMyAcceptor(Long acceptorId) throws Exception {
        List<MatchingDTO.Response> dtoList = new ArrayList<>();
        dtoList = myPageService.getAllMyPageByAcceptorRaw(acceptorId).stream()
                .map(this::mapPageOnlyToDto)
                .filter(Objects::nonNull)
                .toList();
        return dtoList;
    }

    /**
     * MatchingCategory에 따라 DTO 변환 - 임시 코드
     */
    private MatchingDTO.Response mapPageOnlyToDto(PageOnly page) {
        if (page instanceof RedisDuoPageOnly duo) {
            return myPageMapper.toDuoDto(duo);
        } else if (page instanceof RedisScrimPageOnly scrim) {
            return myPageMapper.toScrimDto(scrim);
        }
        throw new GeneralException("지원하지 않는 페이지 타입입니다.");
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
    public boolean existsPageBy(Long memberId, UUID boardUUID) {
        return myPageService.existsPageBy(memberId, boardUUID);
    }

    /**
     * MyPage CRUD 서비스 in MySQL
     * @apiNote : [create] : <strong>백엔드 내부 테스트용.</strong> MySQL 마이페이지 생성. <br>
     *            [read] : 없음. <br>
     *            [delete] : deleteAll
     */
    // Note: 평가하기 마이페이지 조회용
    @Override
    public MyPageDTO.Response readMyPage(Long mypageId) throws Exception {
        MyPage entity = myPageService.readPage(mypageId);
        if(entity.getMatchingCategory() == MatchingCategory.DUO){
            return myPageMapper.toDuoDto((DuoPage) entity);
        } else if(entity.getMatchingCategory() == MatchingCategory.SCRIM){
            return myPageMapper.toScrimDto((ScrimPage) entity);
        }
        else throw new GeneralException("Facade: mypageId로 엔티티 조회 실패.");
    }



    @Override
    public List<MyPageDTO.ResponseMyPage> readMyPageByMemberId(Long memberId) throws Exception {
        return myPageService.readPageSortingByIsReceived(memberId).entrySet().stream()
                .map(entry -> {
                    List<MyPageDTO.Response> filteredDtoList = entry.getValue().stream()
                        .map(myPageMapper::toFilteredDtoList).toList();
                    return myPageMapper.tofilteredWrappeddtoList(filteredDtoList, String.valueOf(entry.getKey()));
                })
                .toList();
    }

    @Override
    public MyPage createPage(UUID mypageUUID) throws Exception {
        PageOnly proj = myPageService.getMyPage(mypageUUID);
        if(proj instanceof RedisDuoPageOnly) {
            return myPageService.createDuoPage(mypageUUID);
        } else if(proj instanceof RedisScrimPageOnly) {
            return myPageService.createScrimPage(mypageUUID);
        }
        throw new GeneralException("Test 실패");
    }

    // Note: 백엔드 내부 테스트용
    @Override
    public void deleteAllPage() throws Exception {
        myPageService.deleteAllDuoMyPage();
        myPageService.deleteAllScrimMyPage();
    }
    @Override
    public List<MyPageDTO.Response> readAllPage() throws Exception {
        List<MyPage> entityList = myPageService.readAllPage();
        return entityList.stream().map(myPageMapper::toFilteredDtoList).toList();
    }

    @Override
    public void delete(Long mypageId) {
        myPageService.deletePage(mypageId);
    }

}
