package com.tools.seoultech.timoproject.matching.service.mapper;


import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static com.tools.seoultech.timoproject.matching.service.mapper.UserMapper.userMapperInstance;

@Mapper(uses = {UserMapper.class}, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    BoardMapper boardMapperInstance = Mappers.getMapper(BoardMapper.class);

    // NOTE: 1. DTO to Redis
    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "userUUID", source = "userUUID")
    RedisBoard.Duo toRedisDuo(BoardDTO.RequestDuo requestDuo, UUID userUUID);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "userUUID", source = "userUUID")
    RedisBoard.Colosseum toRedisColosseum(BoardDTO.RequestColosseum requestColosseum, UUID userUUID);

    // NOTE: 2. Redis to DTO
    @Mapping(target = "boardUUID", source = "uuid")
    @Mapping(target = "responseUserDto", source = "userUUID", qualifiedByName = "searchUserInRedis")
    BoardDTO.ResponseDuo toResponseDuo(RedisBoard redisBoard, @Context RedisUserRepository redisUserRepository);

    @Mapping(target = "boardUUID", source = "uuid")
    @Mapping(target = "responseUserDto", source = "userUUID", qualifiedByName = "searchUserInRedis")
    BoardDTO.ResponseColosseum toColosseum(RedisBoard.Colosseum redisBoard);


    // NOTE: 3. Redis User 조회 및 Response DTO로 반환.
    @Named(value = "searchUserInRedis")
    default UserDTO.Response searchUserInRedis(UUID userUUID, @Context RedisUserRepository redisUserRepository) throws Exception{
        RedisUser redisUser =  redisUserRepository.findById(userUUID).orElseThrow(() -> new Exception("df"));

        if (redisUser instanceof RedisUser.Duo duo) {
            return userMapperInstance.toResponseDuo(duo);
        } else if (redisUser instanceof RedisUser.Colosseum colosseum) {
            return userMapperInstance.toResponseColosseum(colosseum);
        } else {
            throw new IllegalStateException("Unknown RedisUser subtype: " + redisUser.getClass().getName());
        }
    }
}
