package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.matching.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.user.entity.redis.Redis_BaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisTemplate<String, Redis_BaseUser> redisUserTemplate;

    private final UserMapper mapper;
    public Redis_BaseUser dtoToRedis(UserDTO dto){
        Redis_BaseUser redisUser = mapper.dtoToRedis(dto);
        return null;
    }
}
