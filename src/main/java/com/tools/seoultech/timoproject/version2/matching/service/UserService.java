package com.tools.seoultech.timoproject.version2.matching.service;

import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.embeddableType.PartyMemberInfo;
import com.tools.seoultech.timoproject.version2.matching.service.factory.RedisKeyFactory;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisTemplate<String, UserDTO> redisUserTemplate;
    private final RedisTemplate<String, String> redisSubIndexTemplate;
    private final UserMapper mapper;

    public UserDTO dtoToRedisEntity(UserDTO dto){
        // TODO:
        //  1. dto.getMemberId()로 riotAccount 가져오기. @Context 로 This 전달.
        //  2. 사용자의 RiotAccount 기반으로 campactPlayerHistory 가져오기.
        //  3. request Duo/Colosseum > response Duo/Colosseum UserDTO로 conver생성하기. mapper에서?
        return mapper.dtoToRedis(dto, this);
    }

    public UserDTO saveUserInRedis(UserDTO requestDto){
        // NOTE: Redis Key 생성.
        String userKey = RedisKeyFactory.UserKey();
        List<String> keySetList = RedisKeyFactory.getKeySetList(requestDto);

        // NOTE: 저장할 Dto Redis Response화.
        UserDTO entity = dtoToRedisEntity(requestDto);

        // NOTE: Redis 및 보조 인덱스 메모리에 데이터 추가.
        redisUserTemplate.opsForValue().set(userKey, entity, Duration.of(15, ChronoUnit.MINUTES));
        keySetList.forEach(key -> redisSubIndexTemplate.opsForSet().add(userKey, key));

        return entity;
    }
    public UserDTO updateUserInRedis(String UUID, UserDTO requestDto){
        // NOTE: Delete original Entity in Redis
        deleteUserFromRedis(UUID);
        // NOTE: Save new Entity in Redis
        UserDTO entity = saveUserInRedis(requestDto);
        return entity;

    }
    public void deleteUserFromRedis(String UUID){
        UserDTO entity = redisUserTemplate.opsForValue().get(UUID);
        List<String> keySetList = RedisKeyFactory.getKeySetList(entity);
        keySetList.forEach(key -> redisSubIndexTemplate.opsForSet().remove(key, UUID));
    }

    public PartyMemberInfo getRiotInfoOfUser(RiotAccount riotAccount){
        return PartyMemberInfo.builder()
                .riotAccount(riotAccount)
                .userInfo(null)
                .compactPlayerHistory(null)
                .build();
    }
}
