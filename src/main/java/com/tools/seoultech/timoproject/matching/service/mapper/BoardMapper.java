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
    @Mapping(target = "boardUUID",       source = "uuid")
//    @Mapping(target = "responseUserDto", source = "userUUID",
//            qualifiedByName = "searchUserDuo")
    BoardDTO.ResponseDuo toResponseDuo(RedisBoard.Duo redisBoard,
                                       @Context RedisUserRepository redisUserRepository);

    /** RedisBoard.Colosseum → BoardDTO.ResponseColosseum */
    @Mapping(target = "boardUUID",       source = "uuid")
//    @Mapping(target = "responseUserDto", source = "userUUID",
//            qualifiedByName = "searchUserColosseum")
    BoardDTO.ResponseColosseum toResponseColosseum(RedisBoard.Colosseum redisBoard,
                                                   @Context RedisUserRepository redisUserRepository);

    /** Duo 게시글의 userUUID → UserDTO.ResponseDuo 매핑 */
    @Named("searchUserDuo")
    default UserDTO.ResponseDuo searchUserDuo(UUID userUUID,
                                              @Context RedisUserRepository redisUserRepository) {
        RedisUser redisUser = redisUserRepository
                .findById(userUUID)
                .orElseThrow(() -> new RuntimeException("Duo 유저를 찾을 수 없습니다: " + userUUID));

        if (redisUser instanceof RedisUser.Duo duo) {
            // UserMapper를 통해 RedisUser.Duo → UserDTO.ResponseDuo로 변환
            return UserMapper.userMapperInstance.toResponseDuo(duo);
        }
        throw new IllegalStateException(
                "searchUserDuo 호출 시, RedisUser.Duo 타입이 아님: " + redisUser.getClass().getSimpleName());
    }

    /** Colosseum 게시글의 userUUID → UserDTO.ResponseColosseum 매핑 */
    @Named("searchUserColosseum")
    default UserDTO.ResponseColosseum searchUserColosseum(UUID userUUID,
                                                          @Context RedisUserRepository redisUserRepository) {
        RedisUser redisUser = redisUserRepository
                .findById(userUUID)
                .orElseThrow(() -> new RuntimeException("Colosseum 유저를 찾을 수 없습니다: " + userUUID));

        if (redisUser instanceof RedisUser.Colosseum colosseum) {
            // UserMapper를 통해 RedisUser.Colosseum → UserDTO.ResponseColosseum로 변환
            return UserMapper.userMapperInstance.toResponseColosseum(colosseum);
        }
        throw new IllegalStateException(
                "searchUserColosseum 호출 시, RedisUser.Colosseum 타입이 아님: " + redisUser.getClass().getSimpleName());
    }
}