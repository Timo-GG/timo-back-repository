package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.CompactPlayerHistory;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.DuoInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.embeddableType.UserInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.DuoBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.ScrimBoard;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.DuoBoardOnly;
import com.tools.seoultech.timoproject.matching.domain.board.repository.projections.ScrimBoardOnly;
import com.tools.seoultech.timoproject.riot.service.BasicAPIService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper Instance = Mappers.getMapper(BoardMapper.class);


    /** DTO → Redis */
    default DuoBoard toDuoRedis(BoardDTO.RequestDuo dto, @Context BasicAPIService bas){
        CompactPlayerHistory history = getCompactPlayerHistory(dto.memberId(), bas);
        return DuoBoard.of(dto.mapCode(), dto.memo(), history, dto.userInfo(), dto.duoInfo(), dto.memberId());
    }

    default ScrimBoard toScrimRedis(BoardDTO.RequestScrim dto, @Context BasicAPIService bas){
        CompactPlayerHistory history = getCompactPlayerHistory(dto.memberId(), bas);
        return ScrimBoard.of(dto.mapCode(), dto.memo(), dto.headCount(), dto.partyInfo(), history, dto.memberId());
    }

    /** Projection → DTO */
    @Mapping(target = "userInfo", expression = "java(toUserInfo(proj))")
    @Mapping(target = "duoInfo", expression = "java(toUserInfo(proj))")
    BoardDTO.ResponseDuo toDuoDto(DuoBoardOnly proj);

    BoardDTO.ResponseScrim toScrimDto(ScrimBoardOnly proj);


    /** */
    @Named("getHistory")
    default CompactPlayerHistory getCompactPlayerHistory(Long memberId, @Context BasicAPIService bas){
        return new CompactPlayerHistory(
            bas.getSoloRankInfoByPuuid(""),
            bas.getMost3ChampionNames(""),
            bas.getRecentMatchSummaries("")
        );
    }

    default UserInfo toUserInfo(DuoBoardOnly proj) {
        return new UserInfo(proj.getMyPosition(), proj.getMyStyle(), proj.getMyStatus(), proj.getMyVoice());
    }

    default DuoInfo toDuoInfo(DuoBoardOnly proj) {
        return new DuoInfo(proj.getOpponentPosition(), proj.getOpponentStyle());
    }
}