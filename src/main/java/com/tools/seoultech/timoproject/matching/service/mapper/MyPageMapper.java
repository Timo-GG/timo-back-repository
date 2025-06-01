package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CertifiedMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
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
                BoardMapper.Instance.toUserInfo(proj), BoardMapper.Instance.toCertifiedMemberInfo(proj.getMemberId(), proj.getMyPosition(), memberService, bas),
                dto.userInfo(), BoardMapper.Instance.toCertifiedMemberInfo(dto.requestorId(), dto.playPosition(), memberService, bas),
                proj.getMemberId(), dto.requestorId(), dto.boardUUID());
    }

    default RedisScrimPage toScrimMyPage(MatchingDTO.RequestScrim dto, @Context BoardService boardService, @Context RiotAPIService bas, @Context MemberService memberService) throws Exception{
        ScrimBoardOnly proj = boardService.getScrimBoard(dto.boardUUID());

        return RedisScrimPage.of(proj.getHeadCount(), proj.getMapCode(), proj.getMemo(), dto.requestorMemo(),
                BoardMapper.Instance.toCertifiedMemberInfo(proj.getMemberId(), proj.getMyPosition(), memberService, bas), proj.getPartyInfo(),
                BoardMapper.Instance.toCertifiedMemberInfo(dto.requestorId(), dto.playPosition(), memberService, bas) , dto.partyInfo(),
                dto.requestorId(), dto.requestorId(), dto.boardUUID());
    }


    /** Projection → DTO */
    @Mapping(target = "acceptor", expression = "java(toWrappedDuoData(proj, true))")
    @Mapping(target = "requestor", expression = "java(toWrappedDuoData(proj, false))")
    @Mapping(target = "matchingStatus", constant="WAITING")
//    @Mapping(target = "updatedAt", source = "updatedAt")
    MatchingDTO.ResponseDuo toReviewDuoDto(RedisDuoPageOnly proj);

    @Mapping(target = "acceptor", expression = "java(toWrappedScrimData(proj, true))")
    @Mapping(target = "requestor", expression = "java(toWrappedScrimData(proj, false))")
    @Mapping(target = "matchingStatus", constant="WAITING")
//    @Mapping(target = "updatedAt", source = "updatedAt")
    MatchingDTO.ResponseScrim toReviewScrimDto(RedisScrimPageOnly proj);

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
    @Mapping(target = "acceptor", expression = "java(toWrappedDuoData(entity, true))")
    @Mapping(target = "requestor", expression = "java(toWrappedDuoData(entity, false))")
    @Mapping(target = "matchingStatus", constant = "WAITING")
    MatchingDTO.ResponseDuo toReviewDuoDto(RedisDuoPage entity);

    @Mapping(target = "acceptor", expression = "java(toWrappedScrimData(entity, true))")
    @Mapping(target = "requestor", expression = "java(toWrappedScrimData(entity, false))")
    @Mapping(target = "matchingStatus", constant = "WAITING")
    MatchingDTO.ResponseScrim toReviewScrimDto(RedisScrimPage entity);

    /** MySQL → DTO */
    default MyPageDTO.ResponseMyPage tofilteredWrappeddtoList(List<MyPageDTO.Response> filteredDtoList, String format){
        return MyPageDTO.ResponseMyPage.builder()
                .size(filteredDtoList.size())
                .filteredBy(format)
                .dtoList(filteredDtoList).build();
    }

    default MyPageDTO.ResponseDuoReviewPage toReviewDuoDto(DuoPage entity, Boolean IsAcceptor){
        Long mypageId = entity.getId();
        Member member = IsAcceptor ? entity.getRequestor() : entity.getAcceptor();
        CompactMemberInfo myInfo = IsAcceptor ? entity.getRequestorMemberInfo() : entity.getAcceptorMemberInfo();

        return MyPageDTO.ResponseDuoReviewPage.builder()
                .mypageId(mypageId)
                .memberId(member.getMemberId())
                .mapCode(entity.getMapCode())
                .memberInfo(myInfo)
                .matchingCategory(entity.getMatchingCategory())
                .matchingStatus(entity.getMatchingStatus())
                .build();
    }

    default MyPageDTO.ResponseScrimReviewPage toReviewScrimDto(ScrimPage entity, Boolean IsAcceptor){
        Long mypageId = entity.getId();
        Member member = IsAcceptor ? entity.getRequestor() : entity.getAcceptor();
        CompactMemberInfo myInfo = IsAcceptor ? entity.getRequestorMemberInfo() : entity.getAcceptorMemberInfo();

        return MyPageDTO.ResponseScrimReviewPage.builder()
                .mypageId(mypageId)
                .memberId(member.getMemberId())
                .mapCode(entity.getMapCode())
                .memberInfo(myInfo)
                .matchingCategory(entity.getMatchingCategory())
                .matchingStatus(entity.getMatchingStatus())
                .build();
    }
    default MyPageDTO.ResponseDuoPage toDuoDto(DuoPage entity){
        return MyPageDTO.ResponseDuoPage.builder()
                .mypageId(entity.getId())
                .acceptorId(entity.getAcceptor().getMemberId())
                .requestorId(entity.getRequestor().getMemberId())
                .mapCode(entity.getMapCode())
                .acceptorMemberInfo(entity.getAcceptorMemberInfo())
                .requestorMemberInfo(entity.getRequestorMemberInfo())
                .acceptorUserInfo(entity.getAcceptorUserInfo())
                .requestorUserInfo(entity.getRequestorUserInfo())
                .matchingCategory(entity.getMatchingCategory())
                .matchingStatus(entity.getMatchingStatus())
                .build();
    }
    default MyPageDTO.ResponseScrimPage toScrimDto(ScrimPage entity){
        return MyPageDTO.ResponseScrimPage.builder()
                .mypageId(entity.getId())
                .acceptorId(entity.getAcceptor().getMemberId())
                .requestorId(entity.getRequestor().getMemberId())
                .mapCode(entity.getMapCode())
                .matchingCategory(entity.getMatchingCategory())
                .matchingStatus(entity.getMatchingStatus())
                .acceptorMemberInfo(entity.getAcceptorMemberInfo())
                .requestorMemberInfo(entity.getRequestorMemberInfo())
                .acceptorPartyInfo(entity.getAcceptorPartyInfo())
                .requestorPartyInfo(entity.getRequestorPartyInfo())
                .build();
    }

    /** 유틸리티 */
    @Named("toWrappedDuoData")
    default MatchingDTO.WrappedDuoData toWrappedDuoData(Object target, Boolean isAcceptor){
        UserInfo userInfo = null;
        CertifiedMemberInfo memberInfo = null;
        String memo = null;

        if(target instanceof RedisDuoPageOnly proj){
            userInfo = isAcceptor ? proj.getAcceptorUserInfo() : proj.getRequestorUserInfo();
            memberInfo = isAcceptor ? proj.getAcceptorCertifiedMemberInfo() : proj.getRequestorCertifiedMemberInfo();
            memo = isAcceptor ? proj.getAcceptorMemo() : proj.getRequestorMemo();

        } else if(target instanceof RedisDuoPage entity){
            userInfo = isAcceptor ? entity.getAcceptorUserInfo() : entity.getRequestorUserInfo();
            memberInfo = isAcceptor ? entity.getAcceptorCertifiedMemberInfo() : entity.getRequestorCertifiedMemberInfo();
            memo = isAcceptor ? entity.getAcceptorMemo() : entity.getRequestorMemo();

        } else throw new IllegalArgumentException("MyPageMapper의 toWrappedDuoData 에서 target 타입이 잘못되었습니다.");

        return MatchingDTO.WrappedDuoData.builder().userInfo(userInfo).memberInfo(memberInfo).memo(memo).build();
    }

    @Named("toWrappedScrimData")
    default MatchingDTO.WrappedScrimData toWrappedScrimData(Object target, Boolean isAcceptor){
        CertifiedMemberInfo memberInfo = null;
        List<CompactMemberInfo> partyInfo = null;
        String memo = null;

        if(target instanceof RedisScrimPageOnly proj){
            memberInfo = isAcceptor ? proj.getAcceptorCertifiedMemberInfo() : proj.getRequestorCertifiedMemberInfo();
            partyInfo = isAcceptor ? proj.getAcceptorPartyInfo() : proj.getRequestorPartyInfo();
            memo = isAcceptor ? proj.getAcceptorMemo() : proj.getRequestorMemo();

        } else if(target instanceof RedisScrimPage entity){
            memberInfo = isAcceptor ? entity.getAcceptorCertifiedMemberInfo() : entity.getRequestorCertifiedMemberInfo();
            partyInfo = isAcceptor ? entity.getAcceptorPartyInfo() : entity.getRequestorPartyInfo();
            memo = isAcceptor ? entity.getAcceptorMemo() : entity.getRequestorMemo();

        } else throw new IllegalArgumentException("MyPageMapper의 toWrappedScrimData 에서 target 타입이 잘못되었습니다.");

        return MatchingDTO.WrappedScrimData.builder().memberInfo(memberInfo).partyInfo(partyInfo).memo(memo).build();
    }

    @Named("toMember")
    default Member getMember(Long memberId, MemberService memberService){
        return memberService.getById(memberId);
    }
}
