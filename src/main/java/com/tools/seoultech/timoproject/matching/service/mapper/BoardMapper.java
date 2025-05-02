package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.board.dto.SearchBoardDTO;
import com.tools.seoultech.timoproject.matching.board.entity.redis.Redis_BaseSearchBoard;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    Redis_BaseSearchBoard dtoToRedis(SearchBoardDTO requestDto);
    SearchBoardDTO redisToEntity(Redis_BaseSearchBoard requestDto);

}
