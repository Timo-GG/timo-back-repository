package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.DuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.mysql.ScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisDuoPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisScrimPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisDuoPageOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.repository.projections.RedisScrimPageOnly;
import com.tools.seoultech.timoproject.matching.service.BoardService;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MemberService.class, BoardService.class})
public interface MyPageMapper {
    MyPageMapper Instance = Mappers.getMapper(MyPageMapper.class);


    /** Mathing DTO → Redis */
    default RedisDuoPage toDuoRedis(MatchingDTO.RequestDuo dto, @Context BoardService boardService, @Context RiotAPIService bas, @Context MemberService memberService) throws Exception{
        DuoBoardOnly proj = boardService.getDuoBoard(dto.boardUUID());

        return RedisDuoPage.of(proj.getMapCode(), proj.getMemo(), dto.requestorMemo(),
                BoardMapper.Instance.toUserInfo(proj), proj.getMemberInfo(),
                dto.userInfo(), BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas),
                proj.getMemberId(), dto.requestorId(), dto.boardUUID());
    }

    default RedisScrimPage toScrimMyPage(MatchingDTO.RequestScrim dto, @Context BoardService boardService, @Context RiotAPIService bas, @Context MemberService memberService) throws Exception{
        ScrimBoardOnly proj = boardService.getScrimBoard(dto.boardUUID());

        return RedisScrimPage.of(proj.getHeadCount(), proj.getMapCode(), proj.getMemo(), dto.requestorMemo(),
                proj.getMemberInfo(), proj.getPartyInfo(),
                BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas) , dto.partyInfo(),
                dto.requestorId(), dto.requestorId(), dto.boardUUID());
    }


    /** Projection → DTO */
    // TODO: getWrappedDuoData, getWrappedScrimData memo, opponent_memo
    @Mapping(target = "acceptor", expression = "java(getWrappedDuoData(proj.getAcceptorUserInfo(), proj.getAcceptorCertifiedMemberInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedDuoData(proj.getRequestorUserInfo(), proj.getRequestorCertifiedMemberInfo()))")
    @Mapping(target = "matchingStatus", constant="WAITING")
    @Mapping(target = "updatedAt", source = "updatedAt")
    MatchingDTO.ResponseDuo toDuoDto(RedisDuoPageOnly proj);

    @Mapping(target = "acceptor", expression = "java(getWrappedScrimData(proj.getAcceptorCertifiedMemberInfo(), proj.getAcceptorPartyInfo()))")
    @Mapping(target = "requestor", expression = "java(getWrappedScrimData(proj.getRequestorCertifiedMemberInfo(), proj.getRequestorPartyInfo()))")
    @Mapping(target = "matchingStatus", constant="WAITING")
    @Mapping(target = "updatedAt", source = "updatedAt")
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
    default MyPageDTO.ResponseMyPage tofilteredWrappeddtoList(List<MyPageDTO.Response> filteredDtoList, String format){
        return MyPageDTO.ResponseMyPage.builder()
                .size(filteredDtoList.size())
                .filteredBy(format)
                .dtoList(filteredDtoList).build();
    }

    default MyPageDTO.ResponseDuoPage toDuoDto(DuoPage entity, Boolean IsAcceptor){
        Long mypageId = entity.getId();
        Member member = IsAcceptor ? entity.getAcceptor() : entity.getRequestor();
        PlayPosition myPosition = IsAcceptor ? entity.getAcceptorUserInfo().getMyPosition() : entity.getRequestorUserInfo().getMyPosition();
        CompactMemberInfo myInfo = IsAcceptor ? entity.getAcceptorMemberInfo() : entity.getRequestorMemberInfo();
        return MyPageDTO.ResponseDuoPage.builder()
                .mypageId(mypageId)
                .memberId(member.getMemberId())
                .mapCode(entity.getMapCode())
                .myPosition(myPosition)
                .memberInfo((CertifiedMemberInfo) myInfo)
                .matchingCategory(entity.getMatchingCategory())
                .matchingStatus(entity.getMatchingStatus())
                .build();
    }

    default MyPageDTO.ResponseScrimPage toScrimDto(ScrimPage entity, Boolean IsAcceptor){
        Long mypageId = entity.getId();
        Member member = IsAcceptor ? entity.getAcceptor() : entity.getRequestor();
        PlayPosition myPosition = IsAcceptor ? entity.getAcceptorUserInfo().getMyPosition() : entity.getRequestorUserInfo().getMyPosition();
    }

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
