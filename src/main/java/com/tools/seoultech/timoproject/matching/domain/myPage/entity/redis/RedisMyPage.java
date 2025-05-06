package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(value = "MyPage", timeToLive = 15 * 60)
@Getter
public class RedisMyPage {
    private final UUID uuid;

    private final MatchingCategory category;
    private final MatchingStatus status;

    @Reference private final RedisBoard board;
    @Reference private final RedisUser requestor;

    @Transient
    private UUID getAcceptor(){
        return board.getUserUUID();
    }

    @PersistenceCreator
    public RedisMyPage(UUID uuid, MatchingCategory category, MatchingStatus status, RedisBoard board, RedisUser requestor) {
            this.uuid = uuid;
            this.category = category;
            this.status = status;
            this.board = board;
            this.requestor = requestor;
    }
    @Builder
    public RedisMyPage(MatchingCategory category, RedisBoard board, RedisUser requestor) {
            this.uuid = UUID.randomUUID();
            this.category = category;
            this.status = MatchingStatus.WAITING;
            this.board = board;
            this.requestor = requestor;
    }
}
