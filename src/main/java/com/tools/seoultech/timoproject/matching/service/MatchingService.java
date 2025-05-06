package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUserRepository;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import com.tools.seoultech.timoproject.matching.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final RedisTemplate<String, RedisMyPage> myPageRedisTemplate;
    private final RedisTemplate<String, RedisUser> userRedisTemplate;
    private final RedisTemplate<String, RedisBoard> boardRedisTemplate;

    private final UserService userService;
    private final BoardService boardService;

    private final UserMapper userMapper;
    private final MyPageMapper myPageMapper;

    private final RedisBoardRepository redisBoardRepository;
    private final RedisUserRepository redisUserRepository;

    @Transactional
    public RedisMyPage saveDuoInRedis(MatchingDTO.RequestDuo requestMatchingDuo) throws Exception {
        RedisBoard redisDuoBoard = redisBoardRepository.findById(requestMatchingDuo.boardUUID())
                .orElseThrow(() -> new GeneralException("Board UUID에 해당하는 Redis 엔티티가 존재하지 않습니다."));
        RedisUser.Duo redisRequestor = userService.saveDuoUser(requestMatchingDuo.duoRequestorDto());

        if(redisDuoBoard.getCategory() != MatchingCategory.Duo){
            throw new GeneralException("Board와 User의 매칭 카테고리가 일치하지 않습니다.");
        }
        RedisMyPage redisMyPage = myPageMapper.toRedisMyPage(redisDuoBoard, redisRequestor);
        return redisMyPage; // 로직 테스트 후 dto로 변환하여 전달.
    }
    public RedisMyPage getBoardInRedis(UUID myPageUUID) throws Exception {
        RedisMyPage redisMyPage = myPageRedisTemplate.opsForValue().get(myPageUUID);
        if(redisMyPage == null){ throw new GeneralException("없음.");}
        return redisMyPage;
    }
}
