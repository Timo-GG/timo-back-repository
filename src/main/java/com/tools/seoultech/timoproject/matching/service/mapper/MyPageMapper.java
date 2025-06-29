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
                proj.getMemberInfo(), proj.getPartyInfo(),                    // acceptor 정보 (게시글 작성자)
                BoardMapper.Instance.getCertifiedMemberInfo(dto.requestorId(), memberService, bas), dto.partyInfo(),  // requestor 정보 (신청자)
                proj.getMemberId(),  // acceptorId (게시글 작성자 ID)
                dto.requestorId(),   // requestorId (신청자 ID)
                dto.boardUUID());
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
//    MyPageDTO.Response toDuoDto();
    default MyPageDTO.Response toFilteredDtoList(MyPage entity){
        if(entity instanceof DuoPage duoPage){
            var acceptorInfo = MyPageDTO.MyPageWrappedDuoData.builder()
                    .memberInfo(duoPage.getAcceptorMemberInfo())
                    .userInfo(duoPage.getAcceptorUserInfo()) // 수정: getRequestorUserInfo() → getAcceptorUserInfo()
                    .univName(duoPage.getAcceptor().getCertifiedUnivInfo() != null ?
                            duoPage.getAcceptor().getCertifiedUnivInfo().getUnivName() : "미인증")
                    .department(duoPage.getAcceptor().getCertifiedUnivInfo() != null ?
                            duoPage.getAcceptor().getCertifiedUnivInfo().getDepartment() : "미설정")
                    .build();

            var requestorInfo = MyPageDTO.MyPageWrappedDuoData.builder()
                    .memberInfo(duoPage.getRequestorMemberInfo())
                    .userInfo(duoPage.getRequestorUserInfo())
                    .univName(duoPage.getRequestor().getCertifiedUnivInfo() != null ?
                            duoPage.getRequestor().getCertifiedUnivInfo().getUnivName() : "미인증")
                    .department(duoPage.getRequestor().getCertifiedUnivInfo() != null ?
                            duoPage.getRequestor().getCertifiedUnivInfo().getDepartment() : "미설정")
                    .build();

            return MyPageDTO.ResponseDuoPage.builder()
                    .mypageId(duoPage.getId())
                    .mapCode(duoPage.getMapCode())
                    .matchingCategory(duoPage.getMatchingCategory())
                    .matchingStatus(duoPage.getMatchingStatus())
                    .acceptor(acceptorInfo)
                    .requestor(requestorInfo)
                    .acceptorId(duoPage.getAcceptor().getMemberId())
                    .requestorId(duoPage.getRequestor().getMemberId())
                    .acceptorReview(duoPage.getAcceptorReview())
                    .requestorReview(duoPage.getRequestorReview())
                    .reviewStatus(duoPage.getReviewStatus())
                    .createdAt(duoPage.getRegDate())
                    .build();

        } else if (entity instanceof ScrimPage scrimPage){
            var acceptorInfo = MyPageDTO.MyPageWrappedScrimData.builder()
                    .memberInfo(scrimPage.getAcceptorMemberInfo())
                    .partyInfo(scrimPage.getAcceptorPartyInfo())
                    .univName(scrimPage.getAcceptor().getCertifiedUnivInfo() != null ?
                            scrimPage.getAcceptor().getCertifiedUnivInfo().getUnivName() : "미인증")
                    .department(scrimPage.getAcceptor().getCertifiedUnivInfo() != null ?
                            scrimPage.getAcceptor().getCertifiedUnivInfo().getDepartment() : "미설정")
                    .build();

            var requestorInfo = MyPageDTO.MyPageWrappedScrimData.builder()
                    .memberInfo(scrimPage.getRequestorMemberInfo())
                    .partyInfo(scrimPage.getRequestorPartyInfo())
                    .univName(scrimPage.getRequestor().getCertifiedUnivInfo() != null ?
                            scrimPage.getRequestor().getCertifiedUnivInfo().getUnivName() : "미인증")
                    .department(scrimPage.getRequestor().getCertifiedUnivInfo() != null ?
                            scrimPage.getRequestor().getCertifiedUnivInfo().getDepartment() : "미설정")
                    .build();

            return MyPageDTO.ResponseScrimPage.builder()
                    .mypageId(scrimPage.getId())
                    .mapCode(scrimPage.getMapCode())
                    .matchingCategory(scrimPage.getMatchingCategory())
                    .matchingStatus(scrimPage.getMatchingStatus())
                    .acceptor(acceptorInfo)
                    .requestor(requestorInfo)
                    .acceptorId(scrimPage.getAcceptor().getMemberId())
                    .requestorId(scrimPage.getRequestor().getMemberId())
                    .acceptorReview(scrimPage.getAcceptorReview())
                    .requestorReview(scrimPage.getRequestorReview())
                    .reviewStatus(scrimPage.getReviewStatus())
                    .createdAt(scrimPage.getRegDate())
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
    @Mapping(target = "roomId", source = "roomId")
    @Mapping(target = "acceptor", expression = "java(getMyPageWrappedDuoDataWithUniv(entity.getAcceptorUserInfo(), entity.getAcceptorMemberInfo(), entity.getAcceptor()))")
    @Mapping(target = "requestor", expression = "java(getMyPageWrappedDuoDataWithUniv(entity.getRequestorUserInfo(), entity.getRequestorMemberInfo(), entity.getRequestor()))")
    @Mapping(target = "acceptorId", expression = "java(entity.getAcceptor().getMemberId())")
    @Mapping(target = "requestorId", expression = "java(entity.getRequestor().getMemberId())")
    MyPageDTO.ResponseDuoPage toDuoDto(DuoPage entity, Long roomId);

    @Mapping(target = "mypageId", source = "entity.id")
    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "acceptor", expression = "java(getMyPageWrappedDuoDataWithUniv(entity.getAcceptorUserInfo(), entity.getAcceptorMemberInfo(), entity.getAcceptor()))")
    @Mapping(target = "requestor", expression = "java(getMyPageWrappedDuoDataWithUniv(entity.getRequestorUserInfo(), entity.getRequestorMemberInfo(), entity.getRequestor()))")
    @Mapping(target = "acceptorId", expression = "java(entity.getAcceptor().getMemberId())")
    @Mapping(target = "requestorId", expression = "java(entity.getRequestor().getMemberId())")
    @Mapping(target = "acceptorReview", expression = "java(entity.getAcceptorReview())")
    @Mapping(target = "requestorReview", expression = "java(entity.getRequestorReview())")
    @Mapping(target = "reviewStatus", source = "entity.reviewStatus")
    MyPageDTO.ResponseDuoPage toDuoDto(DuoPage entity);


    @Mapping(target = "mypageId", source = "entity.id")
    @Mapping(target = "roomId", source = "roomId")
    @Mapping(target = "acceptor", expression = "java(getMyPageWrappedScrimDataWithUniv(entity.getAcceptorMemberInfo(), entity.getAcceptorPartyInfo(), entity.getAcceptor()))")
    @Mapping(target = "requestor", expression = "java(getMyPageWrappedScrimDataWithUniv(entity.getRequestorMemberInfo(), entity.getRequestorPartyInfo(), entity.getRequestor()))")
    @Mapping(target = "acceptorId", expression = "java(entity.getAcceptor().getMemberId())")
    @Mapping(target = "requestorId", expression = "java(entity.getRequestor().getMemberId())")
    MyPageDTO.ResponseScrimPage toScrimDto(ScrimPage entity, Long roomId);

    @Mapping(target = "mypageId", source = "entity.id")
    @Mapping(target = "roomId", ignore = true)
    @Mapping(target = "acceptor", expression = "java(getMyPageWrappedScrimDataWithUniv(entity.getAcceptorMemberInfo(), entity.getAcceptorPartyInfo(), entity.getAcceptor()))")
    @Mapping(target = "requestor", expression = "java(getMyPageWrappedScrimDataWithUniv(entity.getRequestorMemberInfo(), entity.getRequestorPartyInfo(), entity.getRequestor()))")
    @Mapping(target = "acceptorId", expression = "java(entity.getAcceptor().getMemberId())")
    @Mapping(target = "requestorId", expression = "java(entity.getRequestor().getMemberId())")
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

    @Named("getMyPageWrappedDuoDataWithUniv")
    default MyPageDTO.MyPageWrappedDuoData getMyPageWrappedDuoDataWithUniv(
            UserInfo userInfo,
            CompactMemberInfo memberInfo,
            Member member) {
        return MyPageDTO.MyPageWrappedDuoData.builder()
                .userInfo(userInfo)
                .memberInfo(memberInfo)
                .univName(member.getCertifiedUnivInfo() != null ? member.getCertifiedUnivInfo().getUnivName() : "미인증")
                .department(member.getCertifiedUnivInfo() != null ? member.getCertifiedUnivInfo().getDepartment() : "미설정")
                .build();
    }

    @Named("getMyPageWrappedScrimDataWithUniv")
    default MyPageDTO.MyPageWrappedScrimData getMyPageWrappedScrimDataWithUniv(
            CompactMemberInfo memberInfo,
            List<PartyMemberInfo> partyInfo,
            Member member) {
        return MyPageDTO.MyPageWrappedScrimData.builder()
                .memberInfo(memberInfo)
                .partyInfo(partyInfo)
                .univName(member.getCertifiedUnivInfo() != null ? member.getCertifiedUnivInfo().getUnivName() : "미인증")
                .department(member.getCertifiedUnivInfo() != null ? member.getCertifiedUnivInfo().getDepartment() : "미설정")
                .build();
    }

    @Named("getMember")
    default Member getMember(Long memberId, MemberService memberService){
        return memberService.getById(memberId);
    }
}
