package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import lombok.Getter;
import org.springframework.data.annotation.Reference;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@RedisHash(value = "MyPage", timeToLive = 15 * 60)
@Getter
public class RedisMyPage {
    private final UUID uuid;

    private final MatchingCategory category;

    // Note: UUID 값 참조로 변경하고 싶다면 @Reference를 이용할 것.
    private final RedisBoard board;
    @Reference private final RedisUser requestor;

    @Transient
    private final RedisUser acceptor;

    protected RedisMyPage(UUID uuid, MatchingCategory category, RedisBoard board, RedisUser requestor) {
            this.uuid = uuid;
            this.category = category;
            this.board = board;
            this.requestor = requestor;
            this.acceptor = board.getUser();
    }
}
