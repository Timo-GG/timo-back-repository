package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "MyPage", timeToLive = 15 * 60)
@Getter
public class RedisMyPage {
    @Id
    private final UUID uuid;

    @Indexed
    private final MatchingCategory matchingCategory;
    private final MatchingStatus status;

    @Reference private final RedisBoard board;
    @Reference private final RedisUser requestor;
    @Reference private final RedisUser acceptor;

    // TODO : 수신자 어떻게 처리할지 고민..

    @Transient
    private UUID getAcceptor(){
        return board.getRedisUser().getUuid();
    }

    @PersistenceCreator
    public RedisMyPage(UUID uuid, MatchingCategory matchingCategory, MatchingStatus status, RedisBoard board, RedisUser requestor, RedisUser acceptor) {
        this.uuid = uuid;
        this.matchingCategory = matchingCategory;
        this.status = status;
        this.board = board;
        this.requestor = requestor;
        this.acceptor = acceptor;
    }

    @Builder
    public RedisMyPage(MatchingCategory matchingCategory, RedisBoard board, RedisUser requestor, RedisUser acceptor) {
        this.uuid = UUID.randomUUID();
        this.matchingCategory = matchingCategory;
        this.status = MatchingStatus.WAITING;
        this.board = board;
        this.requestor = requestor;
        this.acceptor = acceptor;
    }
}
