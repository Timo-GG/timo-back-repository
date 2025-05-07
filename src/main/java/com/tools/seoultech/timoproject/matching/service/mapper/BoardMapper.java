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

    /** DTO → Redis 엔티티 */
    @Mapping(target = "redisUser", source = "duoUser")
    RedisBoard.Duo toRedisDuo(BoardDTO.RequestDuo requestDuo, RedisUser.Duo duoUser);

    @Mapping(target = "redisUser", source = "colosseumUser")
    RedisBoard.Colosseum toRedisColosseum(BoardDTO.RequestColosseum requestColosseum, RedisUser.Colosseum colosseumUser);

    /** RedisBoard.Duo → BoardDTO.ResponseDuo */
    @Mapping(target = "boardUUID", source = "uuid")
    @Mapping(target = "responseUserDto", source = "redisUser", qualifiedByName = "mapUserDuo")
    BoardDTO.ResponseDuo toResponseDuo(RedisBoard.Duo redisBoard);

    /** RedisBoard.Colosseum → BoardDTO.ResponseColosseum */
    @Mapping(target = "boardUUID", source = "uuid")
    @Mapping(target = "responseUserDto", source = "redisUser", qualifiedByName = "mapUserColosseum")
    BoardDTO.ResponseColosseum toResponseColosseum(RedisBoard.Colosseum redisBoard);

    /** RedisUser.Duo → UserDTO.ResponseDuo 매핑 */
    @Named("mapUserDuo")
    default UserDTO.ResponseDuo mapUserDuo(RedisUser redisUser) {
        if (redisUser instanceof RedisUser.Duo duo) {
            return UserMapper.userMapperInstance.toResponseDuo(duo);
        }
        throw new IllegalStateException("mapUserDuo 호출 시 RedisUser.Duo가 아님: " + redisUser.getClass().getSimpleName());
    }

    /** RedisUser.Colosseum → UserDTO.ResponseColosseum 매핑 */
    @Named("mapUserColosseum")
    default UserDTO.ResponseColosseum mapUserColosseum(RedisUser redisUser) {
        if (redisUser instanceof RedisUser.Colosseum colosseum) {
            return UserMapper.userMapperInstance.toResponseColosseum(colosseum);
        }
        throw new IllegalStateException("mapUserColosseum 호출 시 RedisUser.Colosseum이 아님: " + redisUser.getClass().getSimpleName());
    }
}