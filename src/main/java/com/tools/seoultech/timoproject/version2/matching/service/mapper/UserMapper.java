package com.tools.seoultech.timoproject.version2.matching.service.mapper;

import com.tools.seoultech.timoproject.version2.matching.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.user.entity.redis.Redis_BaseUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Redis_BaseUser dtoToRedis(UserDTO requestDto);
}
