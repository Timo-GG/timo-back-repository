package com.tools.seoultech.timoproject.version2.matching.board.searvice;

import com.tools.seoultech.timoproject.version2.matching.board.entity.Redis_BaseSearchBoard;
import com.tools.seoultech.timoproject.version2.matching.user.entity.Redis_BaseUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final RedisTemplate<String, Redis_BaseSearchBoard> redisBoardTemplate;
    private final RedisTemplate<String, Redis_BaseUserEntity> redisUserTemplate;

    // TODO:
    //  1. CRUD
    //  2. redis board to myPage
    // 
}
