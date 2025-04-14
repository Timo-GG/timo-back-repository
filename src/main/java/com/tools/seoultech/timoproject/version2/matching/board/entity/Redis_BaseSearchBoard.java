package com.tools.seoultech.timoproject.version2.matching.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("MatchingBoard")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Redis_BaseSearchBoard extends BaseSearchBoard {
    @Id
    private Long id;

    private Long redisBaseUserEntity_id;
    private String memo;

    protected Redis_BaseSearchBoard(Long redisBaseUserEntity_id, String memo) {
        this.redisBaseUserEntity_id = redisBaseUserEntity_id;
        this.memo = memo;
    }
}
