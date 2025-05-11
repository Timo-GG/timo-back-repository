package com.tools.seoultech.timoproject.matching.service.mapper;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper Instance = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "memberAccount", source = "requestDtoDuo.memberId", qualifiedByName = "findMemberAccount")
    @Mapping(target = "matchingCategory", constant = "DUO", resultType = MatchingCategory.class)
    @Mapping(target = "uuid", source = ".", qualifiedByName = "generateUuid")
    RedisUser.Duo toRedisDuo(UserDTO.RequestDuo requestDtoDuo, @Context MemberAccountRepository memberAccountRepository);

    @Mapping(target = "memberAccount", source = "requestDtoColosseum.memberId", qualifiedByName = "findMemberAccount")
    RedisUser.Colosseum toRedisColosseum(UserDTO.RequestColosseum requestDtoColosseum, @Context MemberAccountRepository memberAccountRepository);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseDuo toResponseDuo(RedisUser.Duo redisDuo);

    @Mapping(target = "userUUID", source = "uuid")
    UserDTO.ResponseColosseum toResponseColosseum(RedisUser.Colosseum redisColosseum);

    @Named(value = "findMemberAccount")
    default MemberAccount findMemberAccount(Long memberId, @Context MemberAccountRepository memberAccountRepository){
        return memberAccountRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException("해당 memberId에 해당하는 사용자가 없습니다."));
    }
    @Named("generateUuid")
    default String generateUuid(UserDTO.RequestDuo ignored) {
        return UUID.randomUUID().toString();
    }
}