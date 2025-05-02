package com.tools.seoultech.timoproject.version2.matching.service;

import com.tools.seoultech.timoproject.version2.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis.RedisBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.RedisUserDTO;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.version2.matching.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserService userService;

    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    @Transactional
    public RedisBoardDTO<?> setBoardInRedis(BoardDTO<? extends BoardDTO.Request> boardDto){
        UserDTO<? extends UserDTO.Request> userDto = boardDto.getBody().getUserDtoInRequestBody();
        RedisUserDTO<?> savedUser = userService.setUserInRedis(userDto);
        boardMapper.dtoToRedis(boardDto, savedUser ,this, userMapper);
        return null;
    }
}
