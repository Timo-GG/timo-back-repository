package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper Instance = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "member", source = "requestDtoDuo.memberId", qualifiedByName = "findMemberAccount")
    RedisUser.Duo toRedisDuo(UserDTO.RequestDuo requestDtoDuo, @Context MemberRepository memberRepository);

    @Mapping(target = "member", source = "requestDtoColosseum.memberId", qualifiedByName = "findMemberAccount")
    RedisUser.Colosseum toRedisColosseum(UserDTO.RequestColosseum requestDtoColosseum, @Context MemberRepository memberRepository);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseDuo toResponseDuo(RedisUser.Duo redisDuo);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseColosseum toResponseColosseum(RedisUser.Colosseum redisColosseum);

    @Named(value = "findMemberAccount")
    default Member findMemberAccount(Long memberId, @Context MemberRepository memberRepository){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException("해당 memberId에 해당하는 사용자가 없습니다."));
    }
}