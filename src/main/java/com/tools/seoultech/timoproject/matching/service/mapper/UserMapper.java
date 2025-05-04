package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper userMapperInstance = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "uuid", ignore = true)
    RedisUser.Duo toRedisDuo(UserDTO.RequestDuo requestDtoDuo);

    @Mapping(target = "uuid", ignore = true)
    RedisUser.Colosseum toRedisColosseum(UserDTO.RequestColosseum requestDtoColosseum);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseDuo toResponseDuo(RedisUser.Duo redisDuo);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseColosseum toResponseColosseum(RedisUser.Colosseum redisColosseum);
}
