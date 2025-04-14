package com.tools.seoultech.timoproject.version2.matching.board.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Deprecated
@RedisHash("searchBoard")
@Getter
//@AllArgsConstructor
@NoArgsConstructor
public class Redis_DuoSearchBoard extends Redis_BaseSearchBoard {

    @Builder
    public Redis_DuoSearchBoard(Long userId, String memo){
        super(userId, memo);
    }
}
