package com.tools.seoultech.timoproject.version2.matching.service.mapper;

import com.tools.seoultech.timoproject.version2.matching.domain.board.dto.SearchBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis.RedisSearchBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.service.BoardService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class}, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper2 {
    BoardMapper2 INSTANCE = Mappers.getMapper(BoardMapper2.class);

    // NOTE: 제네릭 타입 (Duo, Colosseum)에 따라 매핑 분기해주는 default 메서드.
    default RedisSearchBoardDTO<?> dtoToRedis(
            SearchBoardDTO<? extends SearchBoardDTO.BoardRequestDTOInterface> dto,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    ) {
        var body = dto.getBody();

        if (body instanceof SearchBoardDTO.RequestDuoBoard) {
            @SuppressWarnings("unchecked") // TODO: DTO 단에서 티입보존 클래스 필드 추가 예정.
            var casted = (SearchBoardDTO<SearchBoardDTO.RequestDuoBoard>) dto;
            var responseBody = toDuoResponse(
                    (SearchBoardDTO.RequestDuoBoard) body,
                    userMapper
            );
            return toRedisFromDuo(casted, responseBody);

        } else if (body instanceof SearchBoardDTO.RequestColosseumBoard) {
            @SuppressWarnings("unchecked") // TODO: DTO 단에서 티입보존 클래스 필드 추가 예정.
            var casted = (SearchBoardDTO<SearchBoardDTO.RequestColosseumBoard>) dto;
            var responseBody = toColosseumResponse((SearchBoardDTO.RequestColosseumBoard)body, userMapper);
            return toRedisFromColosseum(casted, responseBody);

        } else {
            throw new IllegalArgumentException("Unknown board DTO type: " + body.getClass().getName());
        }
    }

    // NOTE: 각 매핑 매서드
    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = "body", source = "duoResponseBody")
    RedisSearchBoardDTO<SearchBoardDTO.ResponseDuoBoard> toRedisFromDuo(
            SearchBoardDTO<SearchBoardDTO.RequestDuoBoard> dto,
            SearchBoardDTO.ResponseDuoBoard duoResponseBody
    );

    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = "body", source = "colosseumResponseBody")
    RedisSearchBoardDTO<SearchBoardDTO.ResponseColosseumBoard> toRedisFromColosseum(
            SearchBoardDTO<SearchBoardDTO.RequestColosseumBoard> dto,
            SearchBoardDTO.ResponseColosseumBoard colosseumResponseBody
    );


    @ObjectFactory
    default SearchBoardDTO.ResponseDuoBoard toDuoResponse(
            SearchBoardDTO.RequestDuoBoard requestBody,
            @Context UserMapper userMapper

    ){

    }
    @ObjectFactory
    default SearchBoardDTO.ResponseColosseumBoard toColosseumResponse(
            SearchBoardDTO.RequestColosseumBoard requestBody,
            @Context UserMapper userMapper
    ){

    }
}
