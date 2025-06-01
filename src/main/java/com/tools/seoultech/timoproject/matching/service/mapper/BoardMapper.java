package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.*;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.Gender;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.PlayPosition;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.BoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.member.service.MemberService;
import com.tools.seoultech.timoproject.ranking.RankingInfo;
import com.tools.seoultech.timoproject.riot.dto.RankInfoDto;
import com.tools.seoultech.timoproject.riot.service.RiotAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper Instance = Mappers.getMapper(BoardMapper.class);


    /** DTO → Redis */
    default DuoBoard toDuoRedis(BoardDTO.RequestDuo dto, @Context MemberService memberService, @Context RiotAPIService bas){
        return DuoBoard.of(dto.mapCode(), dto.memo(), toCertifiedMemberInfo(dto.memberId(), dto.myPosition(), memberService, bas), dto.userInfo(), dto.duoInfo(), dto.memberId());
    }

    default ScrimBoard toScrimRedis(BoardDTO.RequestScrim dto, @Context MemberService memberService, @Context RiotAPIService bas){
        return ScrimBoard.of(dto.mapCode(), dto.memo(), dto.headCount(), toCertifiedMemberInfo(dto.memberId(), dto.myPosition(), memberService, bas), dto.partyInfo(), dto.memberId());
    }

    /** Redis → Redis */
    default DuoBoard toUpdatedEntity(DuoBoard entity, BoardDTO.RequestUpdateDuo dto){
        UserInfo userInfo = dto.userInfo();
        DuoInfo duoInfo = dto.duoInfo();
        return entity.toBuilder()
                .mapCode(dto.mapCode())
                .memo(dto.memo())
                .myPosition(dto.myPosition())
                .myStyle(userInfo.getMyStyle()).myVoice(userInfo.getMyVoice()).myStatus(userInfo.getMyStatus())
                .opponentPosition(duoInfo.getOpponentPosition()).opponentStyle(duoInfo.getOpponentStyle())
                .build();
    }

    default ScrimBoard toUpdatedEntity(ScrimBoard entity, BoardDTO.RequestUpdateScrim dto){
        return entity.toBuilder()
                .mapCode(dto.mapCode())
                .memo(dto.memo())
                .myPosition(dto.myPosition())
                .headCount(dto.headCount())
                .partyInfo(dto.partyInfo())
                .build();
    }

    /** Projection → DTO */
    @Mapping(target = "memberInfo", expression = "java(toCertifiedMemberInfo(proj))")
    @Mapping(target = "userInfo", expression = "java(toUserInfo(proj))")
    @Mapping(target = "duoInfo", expression = "java(toDuoInfo(proj))")
//    @Mapping(target = "updatedAt", source = "updatedAt")
    BoardDTO.ResponseDuo toDuoDto(DuoBoardOnly proj);

    @Mapping(target = "memberInfo", expression = "java(toCertifiedMemberInfo(proj))")
    BoardDTO.ResponseScrim toScrimDto(ScrimBoardOnly proj);


    /** Redis → DTO */
    @Mapping(target = "memberInfo", expression = "java(toCertifiedMemberInfo(entity))")
    @Mapping(target = "userInfo", expression = "java(toUserInfo(entity))")
    @Mapping(target = "duoInfo", expression = "java(toDuoInfo(entity))")
//    @Mapping(target = "updatedAt", source = "updatedAt")
    BoardDTO.ResponseDuo toDuoDto(DuoBoard entity);

    @Mapping(target = "memberInfo", expression = "java(toCertifiedMemberInfo(entity))")
    BoardDTO.ResponseScrim toScrimDto(ScrimBoard entity);


    /** 유틸리티 */
    @Named("toUserInfo")
    default UserInfo toUserInfo(DuoBoardOnly proj) {
        return new UserInfo(proj.getMyStyle(), proj.getMyStatus(), proj.getMyVoice());
    }

    default UserInfo toUserInfo(DuoBoard entity){
        return new UserInfo(entity.getMyStyle(), entity.getMyStatus(), entity.getMyVoice());
    }

    @Named("toDuoInfo")
    default DuoInfo toDuoInfo(DuoBoardOnly proj) {
        return new DuoInfo(proj.getOpponentPosition(), proj.getOpponentStyle());
    }

    default DuoInfo toDuoInfo(DuoBoard entity){
        return new DuoInfo(entity.getOpponentPosition(), entity.getOpponentStyle());
    }

    @Named("toCertifiedMemberInfo")
    default CertifiedMemberInfo toCertifiedMemberInfo(Long memberId, PlayPosition myPosition, @Context MemberService memberService, @Context RiotAPIService bas){
        Member member = memberService.getById(memberId);

        RiotAccount riotAccount = member.getRiotAccount();
        RankInfoDto rankInfoDto = bas.getSoloRankInfoByPuuid(riotAccount.getPuuid());
        List<String> most3Champ = bas.getMost3ChampionNames(riotAccount.getPuuid());
        CertifiedUnivInfo univInfo = member.getCertifiedUnivInfo();
        RankingInfo rankInfo = member.getRankingInfo();

        return new CertifiedMemberInfo(univInfo.getUnivName(), univInfo.getDepartment(), rankInfo.getGender(), rankInfo.getMbti(),
                riotAccount.getPuuid(), riotAccount.getGameName(), riotAccount.getTagLine(), riotAccount.getProfileUrl(),
                rankInfoDto.getTier(), rankInfoDto.getRank(), most3Champ, myPosition);
    }

    default CertifiedMemberInfo toCertifiedMemberInfo(Object target){
        if(target instanceof BoardOnly proj){

            return new CertifiedMemberInfo(proj.getUnivName(), proj.getDepartment(), proj.getGender(), proj.getMbti(),
                    proj.getPuuid(), proj.getGameName(), proj.getTagLine(), proj.getProfileUrl(),
                    proj.getTier(), proj.getRank(), proj.getMost3Champ(), proj.getMyPosition());

        } else if(target instanceof DuoBoard entity){

            return new CertifiedMemberInfo(entity.getUnivName(), entity.getDepartment(), entity.getGender(), entity.getMbti(),
                    entity.getPuuid(), entity.getGameName(), entity.getTagLine(), entity.getProfileUrl(),
                    entity.getTier(), entity.getRank(), entity.getMost3Champ(), entity.getMyPosition());

        } else if(target instanceof ScrimBoard entity){

            return new CertifiedMemberInfo(entity.getUnivName(), entity.getDepartment(), entity.getGender(), entity.getMbti(),
                    entity.getPuuid(), entity.getGameName(), entity.getTagLine(), entity.getProfileUrl(),
                    entity.getTier(), entity.getRank(), entity.getMost3Champ(), entity.getMyPosition());

        } else throw new IllegalArgumentException("BoardMapper의 toCertifiedMemberInfo 에서 target 타입이 잘못되었습니다.");

    }
}