package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper Instance = Mappers.getMapper(UserMapper.class);

    /**
     * uuid는 부모 생성자에서 자동으로 UUID.randomUUID()로 처리되고 있기 때문에, MapStruct가 신경 쓸 필요도 없음
     */

    // 삭제: @Mapping(target = "uuid", ignore = true)
    RedisUser.Duo toRedisDuo(UserDTO.RequestDuo requestDtoDuo);

    // 삭제: @Mapping(target = "uuid", ignore = true)
    RedisUser.Colosseum toRedisColosseum(UserDTO.RequestColosseum requestDtoColosseum);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseDuo toResponseDuo(RedisUser.Duo redisDuo);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseColosseum toResponseColosseum(RedisUser.Colosseum redisColosseum);
}