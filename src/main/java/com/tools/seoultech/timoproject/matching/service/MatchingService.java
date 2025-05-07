package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.global.exception.GeneralException;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MatchingDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.dto.MyPageDTO;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPageRepository;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.service.mapper.MyPageMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final UserService userService;

    private final MyPageMapper myPageMapper;

    private final RedisBoardRepository redisBoardRepository;
    private final RedisMyPageRepository redisMyPageRepository;

    @Transactional
    public RedisMyPage saveDuoMatchingToMyPage(MatchingDTO.RequestDuo matchingDto) {
        RedisBoard redisBoard = redisBoardRepository.findById(matchingDto.boardUUID())
                .orElseThrow(() -> new GeneralException("Board UUID에 해당하는 Redis 엔티티가 존재하지 않습니다."));

        RedisUser redisRequestor = userService.saveDuoUser(matchingDto.duoRequestorDto());

        if(redisBoard.getMatchingCategory() != MatchingCategory.Duo){
            throw new GeneralException("Board와 User의 매칭 카테고리가 일치하지 않습니다.");
        }
        RedisMyPage redisMyPage =  myPageMapper.toRedisMyPage(redisBoard, redisRequestor);
        return redisMyPageRepository.save(redisMyPage); // FIXME: Response 대체.
    }
    public MyPageDTO.ResponseMyPage getMyPage(UUID myPageUUID) throws Exception {
        RedisMyPage redisMyPage = redisMyPageRepository.findById(myPageUUID)
                .orElseThrow(() -> new GeneralException("해당 MyPage는 Redis안에 없습니다."));
        return myPageMapper.toDtoFromRedis(redisMyPage);
    }

    public List<MyPageDTO.ResponseMyPage> getMyPage(MatchingCategory matchingCategory) throws Exception {
        List<RedisMyPage> redisMyPageList = redisMyPageRepository.findAllByMatchingCategory(matchingCategory);
        return redisMyPageList.stream()
                .map(myPageMapper::toDtoFromRedis)
                .toList();
    }

    public void deleteMyPage(UUID myPageUUID) {
        redisMyPageRepository.deleteById(myPageUUID);
    }
    public void deleteAllMyPage(){
        redisMyPageRepository.deleteAll();
    }
}
