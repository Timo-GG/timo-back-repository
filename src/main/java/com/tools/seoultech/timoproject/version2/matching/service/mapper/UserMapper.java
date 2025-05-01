package com.tools.seoultech.timoproject.version2.matching.service.mapper;

import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.service.UserService;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // 1.1 Duo DTO Request to DTO Response
    @Mapping(target = "dto", qualifiedByName = "toDuoResponse")
    @Mapping(target = "memberId", source = "memberId")
    UserDTO<UserDTO.ResponseDuoUser> dtoToRedis(UserDTO<UserDTO.RequestDuoUser> requestDto);

    // 1.2 Colosseum DTO Request to DTO Response
    @Mapping(target = "dto", qualifiedByName = "toColosseumResponse")
    @Mapping(target = "memberId", source = "memberId")
    UserDTO<UserDTO.ResponseColoseumUser> dtoToRedis(
            UserDTO<UserDTO.RequestColosseumUser> requestDto,
            @Context UserService userService
    );

    // 1.3 Generic T to R: 매핑 정의
    @Named("toDuoResponse")
    default UserDTO.ResponseDuoUser toDuoResponse(UserDTO.RequestDuoUser requestDto){
        return UserDTO.ResponseDuoUser.builder()
                .userInfo(requestDto.userInfo())
                .duoInfo(requestDto.duoInfo())
                .build();
    }
    // 1.4 Generic T to R: 매핑 정의
    @Named("toColosseumResponse")
    default UserDTO.ResponseColoseumUser toColosseumResponse(
            UserDTO.RequestColosseumUser requestDto,
            @Context UserService userService
    ) {
        return UserDTO.ResponseColoseumUser.builder()
                .partyMemberInfoList(
                        requestDto.partyMemberRiotAccountList()
                                .stream()
                                .map(userService::getRiotInfoOfUser)
                                .toList()
                ).build();
    }
}
