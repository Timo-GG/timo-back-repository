package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.MyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisDuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {BasicAPIService.class, MemberService.class, BoardService.class})
public interface MyPageMapper {
    MyPageMapper Instance = Mappers.getMapper(MyPageMapper.class);


    /** Mathing DTO → Redis */
    default RedisDuoPage toDuoRedis(MatchingDTO.RequestDuo dto, @Context BoardService boardService, @Context BasicAPIService bas, @Context MemberService memberService) throws Exception{
        DuoBoardOnly proj = boardService.getDuoBoard(dto.boardUUID());

        return RedisDuoPage.of(proj.getMapCode(),
                BoardMapper.Instance.toUserInfo(proj), proj.getMemberInfo(),
                dto.userInfo(), BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas),
                proj.getMemberId(), dto.requestorId(), dto.boardUUID());
    }

    default RedisScrimPage toScrimMyPage(MatchingDTO.RequestScrim dto, @Context BoardService boardService, @Context BasicAPIService bas, @Context MemberService memberService) throws Exception{
        ScrimBoardOnly proj = boardService.getScrimBoard(dto.boardUUID());

        return RedisScrimPage.of(proj.getHeadCount(), proj.getMapCode(),
                proj.getMemberInfo(), proj.getPartyInfo(),
                BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas) , dto.partyInfo(),
                dto.requestorId(), dto.requestorId(), dto.boardUUID());
    }


    /** Projection → DTO */
    @Mapping(target = "acceptor", expression = "java(getWrappedDuoData(proj.getAcceptorUserInfo(), proj.getAcceptorCertifiedMemberInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedDuoData(proj.getRequestorUserInfo(), proj.getRequestorCertifiedMemberInfo()))")
    @Mapping(target = "matchingStatus", constant="WAITING")
    MatchingDTO.ResponseDuo toDuoDto(RedisDuoPageOnly proj);

    @Mapping(target = "acceptor", expression = "java(getWrappedScrimData(proj.getAcceptorCertifiedMemberInfo(), proj.getAcceptorPartyInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedScrimData(proj.getRequestorCertifiedMemberInfo(), proj.getRequestorPartyInfo()))")
    @Mapping(target = "matchingStatus", constant="WAITING")
    MatchingDTO.ResponseScrim toScrimDto(RedisScrimPageOnly proj);

    /** Projection → MySQL */
    @Mapping(target = "acceptor", expression = "java(getMember(proj.getAcceptorId(), memberService))")
    @Mapping(target = "requestor", expression = "java(getMember(proj.getRequestorId(), memberService))")
    @Mapping(target = "acceptorMemberInfo", expression = "java(proj.getAcceptorCertifiedMemberInfo())")
    @Mapping(target = "requestorMemberInfo", expression = "java(proj.getRequestorCertifiedMemberInfo())")
    @Mapping(target = "matchingStatus", constant = "CONNECTED")
    DuoPage toDuoEntity(RedisDuoPageOnly proj, @Context MemberService memberService);

    @Mapping(target = "acceptor", expression = "java(getMember(proj.getAcceptorId(), memberService))")
    @Mapping(target = "requestor", expression = "java(getMember(proj.getRequestorId(), memberService))")
    @Mapping(target = "acceptorMemberInfo", expression = "java(proj.getAcceptorCertifiedMemberInfo())")
    @Mapping(target = "requestorMemberInfo", expression = "java(proj.getRequestorCertifiedMemberInfo())")
    @Mapping(target = "matchingStatus", constant = "CONNECTED")
    ScrimPage toScrimEntity(RedisScrimPageOnly proj, @Context MemberService memberService);

    /** Redis → DTO */
    @Mapping(target = "acceptor", expression = "java(getWrappedDuoData(entity.getAcceptorUserInfo(), entity.getAcceptorCertifiedMemberInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedDuoData(entity.getRequestorUserInfo(), entity.getRequestorCertifiedMemberInfo()))")
    @Mapping(target = "matchingStatus", constant = "WAITING")
    MatchingDTO.ResponseDuo toDuoDto(RedisDuoPage entity);

    @Mapping(target = "acceptor", expression = "java(getWrappedScrimData(entity.getAcceptorCertifiedMemberInfo(), entity.getAcceptorPartyInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedScrimData(entity.getRequestorCertifiedMemberInfo(), entity.getRequestorPartyInfo()))")
    @Mapping(target = "matchingStatus", constant = "WAITING")
    MatchingDTO.ResponseScrim toScrimDto(RedisScrimPage entity);

    /** MySQL → DTO */
//    MyPageDTO.Response toDuoDto();
    default MyPageDTO.Response toFilteredDtoList(MyPage entity){
        if(entity instanceof DuoPage duoPage){
            var acceptorInfo = MatchingDTO.WrappedDuoData.builder().memberInfo(duoPage.getAcceptorMemberInfo()).userInfo(duoPage.getRequestorUserInfo()).build();
            var requestorInfo = MatchingDTO.WrappedDuoData.builder().memberInfo(duoPage.getRequestorMemberInfo()).userInfo(duoPage.getRequestorUserInfo()).build();
            return MyPageDTO.ResponseDuoPage.builder()
                    .mypageId(duoPage.getId())
                    .mapCode(duoPage.getMapCode())
                    .matchingCategory(duoPage.getMatchingCategory())
                    .matchingStatus(duoPage.getMatchingStatus())
                    .acceptor(acceptorInfo)
                    .requestor(requestorInfo)
                    .build();
        } else if (entity instanceof ScrimPage scrimPage){
            var acceptorInfo = MatchingDTO.WrappedScrimData.builder().memberInfo(scrimPage.getAcceptorMemberInfo()).partyInfo(scrimPage.getAcceptorPartyInfo()).build();
            var requestorInfo = MatchingDTO.WrappedScrimData.builder().memberInfo(scrimPage.getRequestorMemberInfo()).partyInfo(scrimPage.getRequestorPartyInfo()).build();
            return MyPageDTO.ResponseScrimPage.builder()
                    .mypageId(scrimPage.getId())
                    .mapCode(scrimPage.getMapCode())
                    .matchingCategory(scrimPage.getMatchingCategory())
                    .matchingStatus(scrimPage.getMatchingStatus())
                    .acceptor(acceptorInfo)
                    .requestor(requestorInfo)
                    .build();
        } else throw new GeneralException("bool fucked");
    }
    default MyPageDTO.ResponseMyPage tofilteredWrappeddtoList(List<MyPageDTO.Response> filteredDtoList, String format){
        return MyPageDTO.ResponseMyPage.builder()
                .size(filteredDtoList.size())
                .filteredBy(format)
                .dtoList(filteredDtoList).build();
    }

    @Mapping(target = "mypageId", source = "entity.id")
    @Mapping(target = "acceptor", expression = "java(getWrappedDuoData(entity.getAcceptorUserInfo(), entity.getAcceptorMemberInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedDuoData(entity.getRequestorUserInfo(), entity.getRequestorMemberInfo()))")
    MyPageDTO.ResponseDuoPage toDuoDto(DuoPage entity);

    @Mapping(target = "mypageId", source = "entity.id")
    @Mapping(target = "acceptor", expression = "java(getWrappedScrimData(entity.getAcceptorMemberInfo(), entity.getAcceptorPartyInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedScrimData(entity.getRequestorMemberInfo(), entity.getRequestorPartyInfo()))")
    MyPageDTO.ResponseScrimPage toScrimDto(ScrimPage entity);


    /** 유틸리티 */
    @Named("getWrappedDuoData")
    default MatchingDTO.WrappedDuoData getWrappedDuoData(UserInfo userInfo, CompactMemberInfo memberInfo){
        return MatchingDTO.WrappedDuoData.builder().userInfo(userInfo).memberInfo(memberInfo).build();
    }

    @Named("getWrappedScrimData")
    default MatchingDTO.WrappedScrimData getWrappedScrimData(CompactMemberInfo memberInfo, List<PartyMemberInfo> partyInfo){
        return MatchingDTO.WrappedScrimData.builder().memberInfo(memberInfo).partyInfo(partyInfo).build();
    }

    @Named("getMember")
    default Member getMember(Long memberId, MemberService memberService){
        return memberService.getById(memberId);
    }
}
