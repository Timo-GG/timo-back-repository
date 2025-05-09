package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = UserMapper.class)
public interface BoardMapper {
    BoardMapper Instance = Mappers.getMapper(BoardMapper.class);

    /** DTO → Redis 엔티티 */
    @Mapping(target = "redisUser", source = "duoUser")
    RedisBoard.Duo toRedisDuo(BoardDTO.RequestDuo requestDuo, RedisUser.Duo duoUser);

    @Mapping(target = "redisUser", source = "colosseumUser")
    RedisBoard.Colosseum toRedisColosseum(BoardDTO.RequestColosseum requestColosseum, RedisUser.Colosseum colosseumUser);

    /** RedisBoard.Duo → BoardDTO.ResponseDuo */
    @Mapping(target = "boardUUID",       source = "uuid")
    @Mapping(
            target = "responseUserDto",
            expression = "java(UserMapper.Instance.toResponseDuo((RedisUser.Duo)redisBoard.getRedisUser()))"
    )
    BoardDTO.ResponseDuo toResponseDuo(RedisBoard.Duo redisBoard);

    /** RedisBoard.Colosseum → BoardDTO.ResponseColosseum */
    @Mapping(target = "boardUUID",       source = "uuid")
    @Mapping(
            target = "responseUserDto",
            expression = "java(UserMapper.Instance.toResponseColosseum((RedisUser.Colosseum)redisBoard.getRedisUser()))"
    )
    BoardDTO.ResponseColosseum toResponseColosseum(RedisBoard.Colosseum redisBoard);
}