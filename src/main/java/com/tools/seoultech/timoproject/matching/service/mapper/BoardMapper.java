package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import org.mapstruct.*;

import java.util.UUID;

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
    BoardDTO.ResponseDuo toResponseDuo(RedisBoard.Duo redisBoard,
                                       @Context RedisUserRepository redisUserRepository);

    /** RedisBoard.Colosseum → BoardDTO.ResponseColosseum */
    @Mapping(target = "boardUUID",       source = "uuid")
    BoardDTO.ResponseColosseum toResponseColosseum(RedisBoard.Colosseum redisBoard,
                                                   @Context RedisUserRepository redisUserRepository);

}