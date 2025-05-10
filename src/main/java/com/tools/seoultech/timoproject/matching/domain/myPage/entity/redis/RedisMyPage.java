package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import io.swagger.v3.oas.annotations.media.Schema;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "matchingCategory")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisBoard.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisBoard.Colosseum.class, name = "COLOSSEUM"),
        @JsonSubTypes.Type(value = RedisUser.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisUser.Colosseum.class, name = "COLOSSEUM")
})
@Getter
public class RedisMyPage {
    @Id
    private final UUID uuid;

    @Indexed
    private final MatchingCategory matchingCategory;
    private final MatchingStatus status;

    @Reference
    @Schema(type = "object", oneOf = { RedisBoard.Duo.class, RedisBoard.Colosseum.class})
    private final RedisBoard board;

    @Reference
    @Schema(type = "object", oneOf = { RedisUser.Duo.class, RedisUser.Colosseum.class})
    private final RedisUser requestor;

    @Transient
    private UUID getAcceptor(){
        return board.getRedisUser().getUuid();
    }

    @PersistenceCreator
    public RedisMyPage(UUID uuid, MatchingCategory matchingCategory, MatchingStatus status, RedisBoard board, RedisUser requestor) {
        this.uuid = uuid;
        this.matchingCategory = matchingCategory;
        this.status = status;
        this.board = board;
        this.requestor = requestor;
    }

    @Builder
    public RedisMyPage(MatchingCategory matchingCategory, RedisBoard board, RedisUser requestor) {
        this.uuid = UUID.randomUUID();
        this.matchingCategory = matchingCategory;
        this.status = MatchingStatus.WAITING;
        this.board = board;
        this.requestor = requestor;
    }
}
