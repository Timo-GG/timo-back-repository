package com.tools.seoultech.timoproject.matching.service;

import com.tools.seoultech.timoproject.matching.domain.board.dto.BoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardDTO;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardRepository;
import com.tools.seoultech.timoproject.matching.domain.user.dto.UserDTO;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import com.tools.seoultech.timoproject.matching.service.mapper.BoardMapper;
import com.tools.seoultech.timoproject.matching.service.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserService userService;
    private final RedisBoardRepository redisBoardRepository;

    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    @Transactional
    public RedisBoardDTO<?> setBoardInRedis(BoardDTO<? extends BoardDTO.Request> boardDto){
        UserDTO<? extends UserDTO.Request> userDto = boardDto.body().getUserDtoInRequestBody();
        RedisUser<?> savedUser = userService.setUserInRedis(userDto);
        RedisBoardDTO<?> savedBoard = boardMapper.dtoToRedis(boardDto, savedUser , this, userMapper);
        redisBoardRepository.save(savedBoard);
        return savedBoard;
    }

    /**
     * uuid를 기반으로 Redis에서 듀오 게시글을 조회
     * @param uuid : Redis에 저장된 게시글의 고유 ID
     * @return : RedisBoardDTO<ResponseDuo>
     */
    public RedisBoardDTO<BoardDTO.ResponseDuo> getDuoBoardFromRedis(String uuid) {
        RedisBoardDTO<?> result = redisBoardRepository.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "해당 uuid에 대한 듀오 게시글이 존재하지 않습니다."));
        if (!(result.getBody() instanceof BoardDTO.ResponseDuo)) {
            throw new ResponseStatusException(NOT_FOUND, "uuid는 존재하지만 듀오 게시글이 아닙니다.");
        }

        @SuppressWarnings("unchecked")
        RedisBoardDTO<BoardDTO.ResponseDuo> duoBoard = (RedisBoardDTO<BoardDTO.ResponseDuo>) result;
        return duoBoard;
    }

    /**
     * 듀오 게시글 전체 목록 조회
     */
    public List<RedisBoardDTO<BoardDTO.ResponseDuo>> listAllDuoBoards() {
        return StreamSupport.stream(redisBoardRepository.findAll().spliterator(), false)
                .filter(dto -> dto.getBody() instanceof BoardDTO.ResponseDuo)
                .map(dto -> (RedisBoardDTO<BoardDTO.ResponseDuo>) dto)
                .toList();
    }

    /** 듀오 게시글 삭제 */
    @Transactional
    public void deleteDuoBoard(String uuid) {
        if (!redisBoardRepository.existsById(uuid)) {
            throw new ResponseStatusException(NOT_FOUND, "삭제할 듀오 게시글이 없습니다.");
        }
        redisBoardRepository.deleteById(uuid);
    }
}