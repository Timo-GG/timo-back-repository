package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.*;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper Instance = Mappers.getMapper(BoardMapper.class);


    /** DTO → Redis */
    default DuoBoard toDuoRedis(BoardDTO.RequestDuo dto, @Context MemberService memberService, @Context BasicAPIService bas){
        return DuoBoard.of(dto.mapCode(), dto.memo(), getCertifiedMemberInfo(dto.memberId(), memberService, bas), dto.userInfo(), dto.duoInfo(), dto.memberId());
    }

    default ScrimBoard toScrimRedis(BoardDTO.RequestScrim dto, @Context MemberService memberService, @Context BasicAPIService bas){
        return ScrimBoard.of(dto.mapCode(), dto.memo(), dto.headCount(), getCertifiedMemberInfo(dto.memberId(), memberService, bas), dto.partyInfo(), dto.memberId());
    }

    /** Redis → Redis */
    default DuoBoard toUpdatedEntity(DuoBoard entity, BoardDTO.RequestUpdateDuo dto){
        return DuoBoard.builder()
                .boardUUID(entity.getBoardUUID())
                .mapCode(dto.mapCode()).memo(dto.memo()).memberInfo(entity.getMemberInfo())
                .myPosition(dto.userInfo().getMyPosition()).myVoice(dto.userInfo().getMyVoice())
                .myStyle(dto.userInfo().getMyStyle()).myStatus(dto.userInfo().getMyStatus())
                .opponentPosition(dto.duoInfo().getOpponentPosition()).opponentStyle(dto.duoInfo().getOpponentStyle())
                .memberId(entity.getMemberId()).matchingCategory(MatchingCategory.DUO).tier(entity.getTier()).build();
    }

    default ScrimBoard toUpdatedEntity(ScrimBoard entity, BoardDTO.RequestUpdateScrim dto){
        return ScrimBoard.builder()
                .boardUUID(entity.getBoardUUID())
                .mapCode(dto.mapCode()).memo(dto.memo()).headCount(dto.headCount())
                .memberInfo(entity.getMemberInfo()).partyInfo(dto.partyInfo())
                .memberId(entity.getMemberId()).matchingCategory(MatchingCategory.SCRIM).tier(entity.getTier()).build();

    }

    /** Projection → DTO */
    @Mapping(target = "userInfo", expression = "java(toUserInfo(proj))")
    @Mapping(target = "duoInfo", expression = "java(toDuoInfo(proj))")
    BoardDTO.ResponseDuo toDuoDto(DuoBoardOnly proj);
    BoardDTO.ResponseScrim toScrimDto(ScrimBoardOnly proj);

    /** Redis → DTO */
    @Mapping(target = "userInfo", expression = "java(toUserInfo(entity))")
    @Mapping(target = "duoInfo", expression = "java(toDuoInfo(entity))")
    BoardDTO.ResponseDuo toDuoDto(DuoBoard entity);
    BoardDTO.ResponseScrim toScrimDto(ScrimBoard entity);


    /** 유틸리티 */
    default CertifiedMemberInfo getCertifiedMemberInfo(Long memberId, @Context MemberService memberService, @Context BasicAPIService bas){
        Member member = memberService.getById(memberId);
        RiotAccount riotAccount = member.getRiotAccount();
        RankInfoDto rankInfo = bas.getSoloRankInfoByPuuid(riotAccount.getPuuid());
        List<String> most3Champ = bas.getMost3ChampionNames(riotAccount.getPuuid());
        return new CertifiedMemberInfo(member.getCertifiedUnivInfo().getUnivName(), riotAccount, rankInfo, most3Champ);
    }

    @Named("toUserInfo")
    default UserInfo toUserInfo(DuoBoardOnly proj) {
        return new UserInfo(proj.getMyPosition(), proj.getMyStyle(), proj.getMyStatus(), proj.getMyVoice());
    }

    default UserInfo toUserInfo(DuoBoard entity){
        return new UserInfo(entity.getMyPosition(), entity.getMyStyle(), entity.getMyStatus(), entity.getMyVoice());
    }

    @Named("toDuoInfo")
    default DuoInfo toDuoInfo(DuoBoardOnly proj) {
        return new DuoInfo(proj.getOpponentPosition(), proj.getOpponentStyle());
    }

    default DuoInfo toDuoInfo(DuoBoard entity){
        return new DuoInfo(entity.getOpponentPosition(), entity.getOpponentStyle());
    }
}