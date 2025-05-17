package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.DuoMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.ScrimMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.repository.projections.DuoMyPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.repository.projections.ScrimMyPageOnly;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {BasicAPIService.class, MemberService.class, BoardService.class})
public interface MyPageMapper {
    MyPageMapper Instance = Mappers.getMapper(MyPageMapper.class);


    /** DTO → Redis */
    default DuoMyPage toDuoRedis(MatchingDTO.RequestDuo dto, @Context BoardService boardService, @Context BasicAPIService bas, @Context MemberService memberService) throws Exception{
        DuoBoardOnly proj = boardService.getDuoBoard(dto.boardUUID());

        return DuoMyPage.of(BoardMapper.Instance.toUserInfo(proj), proj.getMemberInfo(),
                dto.userInfo(), BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas),
                proj.getMemberId(), dto.requestorId(), dto.boardUUID());
    }

    default ScrimMyPage toScrimMyPage(MatchingDTO.RequestScrim dto, @Context BoardService boardService, @Context BasicAPIService bas, @Context MemberService memberService) throws Exception{
        ScrimBoardOnly proj = boardService.getScrimBoard(dto.boardUUID());

        return ScrimMyPage.of(proj.getMemberInfo(), proj.getPartyInfo(),
                BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas) , dto.partyInfo(),
                dto.requestorId(), dto.requestorId(), dto.boardUUID());
    }


    /** Projection → DTO */
    @Mapping(target = "acceptor", expression = "java(getWrappedDuoData(proj, true))")
    @Mapping(target = "requestor", expression = "java(getWrappedDuoData(proj, false))")
    MatchingDTO.ResponseDuo toDuoDto(DuoMyPageOnly proj);

    @Mapping(target = "acceptor", expression = "java(getWrappedScrimData(proj, true))")
    @Mapping(target = "requestor", expression = "java(getWrappedScrimData(proj, false))")
    MatchingDTO.ResponseScrim toScrimDto(ScrimMyPage proj);

    /** 조회용 */
    MyPageDTO.ResponseMyPage toFilteredDtoList( Integer sizeOfDuo,
                                                Integer sizeOfScrim,
                                                List<MatchingDTO.ResponseDuo> duoList,
                                                List<MatchingDTO.ResponseScrim> scrimList
    );


    /** 유틸리티 */
    @Named("getWrappedDuoData")
    default MatchingDTO.WrappedDuoData getWrappedDuoData(DuoMyPageOnly proj, Boolean isAcceptor){
        if(isAcceptor){
            return MatchingDTO.WrappedDuoData.builder()
                    .userInfo(proj.getAcceptorUserInfo())
                    .memberInfo(proj.getAcceptorCertifiedMemberInfo())
                    .build();
        } else {
            return MatchingDTO.WrappedDuoData.builder()
                    .userInfo(proj.getRequestorUserInfo())
                    .memberInfo(proj.getRequesterCertifiedMemberInfo())
                    .build();
        }
    }

    @Named("getWrappedScrimData")
    default MatchingDTO.WrappedScrimData getWrappedScrimData(ScrimMyPageOnly proj, Boolean isAcceptor){
        if(isAcceptor) {
            return MatchingDTO.WrappedScrimData.builder()
                    .memberInfo(proj.getAcceptorCertifiedMemberInfo())
                    .partyInfo(proj.getAcceptorPartyInfo())
                    .build();
        } else {
            return MatchingDTO.WrappedScrimData.builder()
                    .partyInfo(proj.getRequestorPartyInfo()).build();
        }
    }
}
