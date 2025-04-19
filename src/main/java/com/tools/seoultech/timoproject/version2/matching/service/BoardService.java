package com.tools.seoultech.timoproject.version2.matching.service;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.version2.matching.board.dto.SearchBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.board.entity.mysql.BaseSearchBoard;
import com.tools.seoultech.timoproject.version2.matching.board.entity.redis.Redis_BaseSearchBoard;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.UserMapper;
import com.tools.seoultech.timoproject.version2.matching.user.entity.redis.Redis_BaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final RedisTemplate<String, Redis_BaseSearchBoard> redisBoardTemplate;
    private final RedisTemplate<String, Redis_BaseUser> redisUserTemplate;

    private final UserService userService;

    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    public Redis_BaseSearchBoard dtoToRedis(SearchBoardDTO dto) {
        // MapStruct mapper 사용할 것.
        // redis ID 필드 <> mysql ID 필드 변환 로직 구현.
        // 자세한건 post 서비스것과 비교해보면서 파악할 것.
        boardMapper.dtoToRedis(dto);
        return null;
    }

    public BaseSearchBoard redisToEntity(Redis_BaseSearchBoard dto){
        return null;
    }

    public void CreateDuoBoardInRedis(
            @CurrentMemberId Long memberId,
            SearchBoardDTO requestDto
    ) {
        // TODO #1 : 레디스에 UserEntity template 생성 및 KEY_ID 값 반환.
        Redis_BaseUser redisUser = userService.dtoToRedis(null);
        // TODO #2 : 레디스에 SearchBoard template 생성 및 KEY_ID 값 반환.
        Redis_BaseSearchBoard redisBoard = this.dtoToRedis(requestDto);

    }
}
