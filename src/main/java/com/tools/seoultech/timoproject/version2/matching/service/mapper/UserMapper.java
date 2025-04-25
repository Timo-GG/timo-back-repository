package com.tools.seoultech.timoproject.version2.matching.service.mapper;

import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql.ColosseumUser;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.mysql.DuoUser;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.Redis_BaseUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping()
    DuoUser dtoToEntity(UserDTO<UserDTO.RequestDuoUser> requestDto);

    @Mapping()
    ColosseumUser dtoToEntity(UserDTO<UserDTO.RequestColosseumUser> requestDto);

}
