package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import org.springframework.data.annotation.PersistenceCreator;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;


@RedisHash(value = "redisBoard", timeToLive = 15 * 60)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "matchingCategory",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisBoard.Duo.class, name = "Duo"),
        @JsonSubTypes.Type(value = RedisBoard.Colosseum.class, name = "Colosseum")
})
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RedisBoard {
    @Id
    private final UUID uuid;
    private final UUID userUUID;

    private final String memo;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    @Getter
    public static class Duo extends RedisBoard{
        @Builder
        @PersistenceCreator
        public Duo(UUID userUUID, String memo) {
            super(UUID.randomUUID(), userUUID, memo, MatchingCategory.Duo);
        }
    }

    @Getter
    public static class Colosseum extends RedisBoard{
        private ColosseumMapCode mapCode;
        private Integer headCount;

        @Builder
        @PersistenceCreator
        public Colosseum(UUID userUUID, String memo, ColosseumMapCode mapCode, Integer headCount) {
            super(UUID.randomUUID(), userUUID, memo, MatchingCategory.Colosseum);
            this.mapCode = mapCode;
            this.headCount = headCount;
        }
    }
}
