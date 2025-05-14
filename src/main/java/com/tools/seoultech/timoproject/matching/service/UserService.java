package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.memberAccount.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUserRepository redisUserRepository;
    private final UserMapper userMapper;
    private final MemberRepository memberRepository;

    /**
     * Duo 사용자 정보를 Redis에 저장
     */
    public RedisUser.Duo saveDuoUser(UserDTO.RequestDuo requestDuo) {
        RedisUser.Duo redisUser = userMapper.toRedisDuo(requestDuo, memberRepository);
        return redisUserRepository.save(redisUser);
    }

    /**
     * Colosseum 사용자 정보를 Redis에 저장
     */
    public RedisUser.Colosseum saveColosseumUser(UserDTO.RequestColosseum requestColosseum) {
        RedisUser.Colosseum redisUser = userMapper.toRedisColosseum(requestColosseum, memberRepository);
        return redisUserRepository.save(redisUser);
    }

    /**
     * Redis에서 Duo 사용자 정보 조회
     */
    public UserDTO.ResponseDuo getDuoUser(UUID userUUID) throws Exception {
        RedisUser redisUser = redisUserRepository.findById(userUUID).orElseThrow(() -> new Exception("User not found"));
        return userMapper.toResponseDuo((RedisUser.Duo) redisUser);
    }

    /**
     * Redis에서 Colosseum 사용자 정보 조회
     */
    public UserDTO.ResponseColosseum getColosseumUser(UUID userUUID) throws Exception {
        RedisUser redisUser = redisUserRepository.findById(userUUID).orElseThrow(() -> new Exception("User not found"));
        return userMapper.toResponseColosseum((RedisUser.Colosseum) redisUser);
    }

    /**
     * UUID로 사용자 삭제
     */
    public void deleteUserByUUID(UUID userUUID) throws Exception {
        RedisUser redisUser = redisUserRepository.findById(userUUID)
                .orElseThrow(() -> new Exception("User not found"));
        redisUserRepository.delete(redisUser);
    }
}
