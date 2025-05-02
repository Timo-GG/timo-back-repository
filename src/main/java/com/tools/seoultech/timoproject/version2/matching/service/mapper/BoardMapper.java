package com.tools.seoultech.timoproject.version2.matching.service.mapper;

import com.tools.seoultech.timoproject.version2.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis.RedisBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.RedisUserDTO;
import com.tools.seoultech.timoproject.version2.matching.service.BoardService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class}, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    // NOTE: 제네릭 타입 (Duo, Colosseum)에 따라 매핑 분기해주는 default 메서드.
    default RedisBoardDTO<?> dtoToRedis(
            BoardDTO<? extends BoardDTO.Request> dto,
            RedisUserDTO<? extends UserDTO.Response> user,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    ) {
        var body = dto.getBody();

        if (body instanceof BoardDTO.RequestDuo) {
            @SuppressWarnings("unchecked") // TODO: DTO 단에서 티입보존 클래스 필드 추가 예정.
            var casted = (BoardDTO<BoardDTO.RequestDuo>) dto;
            return toRedisFromDuo(casted, (RedisUserDTO<UserDTO.ResponseDuo>)user, boardService, userMapper);

        } else if (body instanceof BoardDTO.RequestColosseum) {
            @SuppressWarnings("unchecked") // TODO: DTO 단에서 티입보존 클래스 필드 추가 예정.
            var casted = (BoardDTO<BoardDTO.RequestColosseum>) dto;
            return toRedisFromColosseum(casted, (RedisUserDTO<UserDTO.ResponseColosseum>)user, boardService, userMapper);

        } else {
            throw new IllegalArgumentException("Unknown board DTO type: " + body.getClass().getName());
        }
    }

    // NOTE: 각 매핑 매서드
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = "body", source = "dto.body", qualifiedByName = "toResponseDuo")
    RedisBoardDTO<BoardDTO.ResponseDuo> toRedisFromDuo
    (
            BoardDTO<BoardDTO.RequestDuo> dto,
            RedisUserDTO<UserDTO.ResponseDuo> user,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    );

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = "userUUID", expression = "java(user.getUuid())")
    @Mapping(target = "body", source = "dto.body", qualifiedByName = "toResponseColosseum")
    RedisBoardDTO<BoardDTO.ResponseColosseum> toRedisFromColosseum
    (
            BoardDTO<BoardDTO.RequestColosseum> dto,
            RedisUserDTO<UserDTO.ResponseColosseum> user,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    );


    @Named("toResponseDuo")
    default BoardDTO.ResponseDuo toResponseDuo(
            BoardDTO.RequestDuo requestBody,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    ){
        RedisUserDTO<UserDTO.ResponseDuo> responseUser = userMapper.toRedisFromDuo(requestBody.requestUserDto());
        BoardDTO.ResponseDuo.builder()
                .responseUserDto(null)
                .compactPlayerHistory(null)
                .build();
        return null;
        // NOTE:
        //  1. redis Board 엔티티에 담기는 userDTO 는 redisDTO가 아닌 ResponseDTO.
        //  2. redis User 엔티티에 대한 참조를 userUUID로 담음.

        // NOTE: board request dto >>> (user request dto -> user redis dto -> user response dto)  >>> board redis dto
        //  1. 각 redis 도메인에는 다른 레디스 도메인이 알 필요없는 메타 데이터도 담긴다는 의미로 활용 (확장성).
    }
    @Named("toResponseColosseum")
    default BoardDTO.ResponseColosseum toResponseColosseum(
            BoardDTO.RequestColosseum requestBody,
            @Context BoardService boardService,
            @Context UserMapper userMapper
    ){
        return null;
    }
}
