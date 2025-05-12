package com.tools.seoultech.timoproject.matching.service.indexInitilizer;

import com.redis.om.spring.indexing.RediSearchIndexer;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoardRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisBoardIndexInitializer {

    private final RediSearchIndexer entityIndexer;
    private final RedisBoardRepository repository;

    @PostConstruct
    public void createRedisBoardIndex() {
        entityIndexer.createIndicesFor(RedisBoard.class);
    }
}

