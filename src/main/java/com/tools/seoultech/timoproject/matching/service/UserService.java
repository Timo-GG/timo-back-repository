package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import com.tools.seoultech.timoproject.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisUserRepository redisUserRepository;
    private final UserMapper mapper;

    public RedisUser<?> setUserInRedis(UserDTO<? extends UserDTO.Request> requestDto){
        RedisUser<? extends UserDTO.Response> redisUser = mapper.dtoToRedis(requestDto, this);
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
