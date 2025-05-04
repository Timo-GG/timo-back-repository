package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserDTO;
import com.tools.seoultech.timoproject.matching.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    default RedisUserDTO<?> dtoToRedis(
            UserDTO<? extends UserDTO.Request> dto,
            @Context UserService userService
    ){
        var body = dto.getBody();

        if(body instanceof UserDTO.RequestDuo){
            @SuppressWarnings("unchecked")
            var casted = (UserDTO<UserDTO.RequestDuo>) dto;
            return toRedisFromDuo(casted);

        } else if(body instanceof UserDTO.RequestColosseum){
            @SuppressWarnings("unchecked")
            var casted = (UserDTO<UserDTO.RequestColosseum>) dto;
            return toRedisFromColosseum(casted, userService);
        } else {
            throw new IllegalArgumentException("Request 타입이 아닌 Record 입니다.");
        }
    }
    default UserDTO<? extends UserDTO.Response> redisToDto(RedisUserDTO<? extends UserDTO.Response> redis){
        var body = redis.getBody();
        if(body instanceof UserDTO.ResponseDuo){
            @SuppressWarnings("unchecked")
            var casted = (RedisUserDTO<UserDTO.ResponseDuo>) redis;
            return toDtoFromDuo(casted);
        } else if(body instanceof UserDTO.ResponseColosseum){
            return null;
        } else {
            return null;

        }
    }

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = ".", source = "body", qualifiedByName = "toResponseDuo")
    RedisUserDTO<UserDTO.ResponseDuo> toRedisFromDuo(UserDTO<UserDTO.RequestDuo> dto);

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "matchingCategory", ignore = true)
    @Mapping(target = ".", source = "body", qualifiedByName = "toResponseColosseum")
    RedisUserDTO<UserDTO.ResponseColosseum> toRedisFromColosseum(UserDTO<UserDTO.RequestColosseum> dto, @Context UserService userService);

    UserDTO<UserDTO.ResponseDuo> toDtoFromDuo(RedisUserDTO<UserDTO.ResponseDuo> redis);

    @Named("toResponseDuo")
    default UserDTO.ResponseDuo toResponseDuo(UserDTO.RequestDuo requestDuo){
        return UserDTO.ResponseDuo.builder()
                .userInfo(requestDuo.userInfo())
                .duoInfo(requestDuo.duoInfo())
                .build();
    }
    default UserDTO.ResponseColosseum toResponseColosseum
    (
            UserDTO.RequestColosseum requestColosseum, @Context UserService userService
    ){
        List<PartyMemberInfo> partyMemberInfoList = requestColosseum
                .partyMemberRiotAccountList().stream()
                .map(userService::getRiotInfoOfUser)
                .toList();
        return UserDTO.ResponseColosseum.builder()
                .partyMemberInfoList(partyMemberInfoList)
                .build();
    }
}
