package com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis;

import com.tools.seoultech.timoproject.global.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("MatchingBoard")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Redis_BaseSearchBoard extends BaseEntity {
    @Id
    private Long id;

    private Long redisBaseUserEntity_id;

    private String memo;

    protected Redis_BaseSearchBoard(Long redisBaseUserEntity_id, String memo) {
        this.redisBaseUserEntity_id = redisBaseUserEntity_id;
        this.memo = memo;
    }
}
