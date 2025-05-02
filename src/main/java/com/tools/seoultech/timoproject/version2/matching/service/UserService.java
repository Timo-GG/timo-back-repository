package com.tools.seoultech.timoproject.version2.matching.service;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.RedisUserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.RedisUserRepository;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisUserRepository redisUserRepository;
    private final UserMapper mapper;

    public RedisUserDTO<?> setUserInRedis(UserDTO<? extends UserDTO.Request> requestDto){
        RedisUserDTO<? extends UserDTO.Response> redisUser = mapper.dtoToRedis(requestDto, this);
        return redisUserRepository.save(redisUser);
    }

    public PartyMemberInfo getRiotInfoOfUser(RiotAccount riotAccount){
        return PartyMemberInfo.builder()
                .riotAccount(riotAccount)
                .userInfo(null)
                .compactPlayerHistory(null)
                .build();
    }
}
