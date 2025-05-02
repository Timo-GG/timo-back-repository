package com.tools.seoultech.timoproject.matching.service;



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
