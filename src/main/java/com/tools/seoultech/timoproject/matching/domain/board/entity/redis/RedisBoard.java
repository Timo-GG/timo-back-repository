package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import org.springframework.data.annotation.PersistenceCreator;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
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
public abstract class RedisBoard {
    @Id
    private final UUID uuid;

    private final UUID userUUID;
    private final String memo;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private final MatchingCategory matchingCategory;

    /** 역직렬화용 생성자 : Redis 조회 시 사용 */
    @PersistenceCreator
    protected RedisBoard(UUID uuid,
                         UUID userUUID,
                         String memo,
                         MatchingCategory matchingCategory) {
        this.uuid             = uuid;
        this.userUUID         = userUUID;
        this.memo             = memo;
        this.matchingCategory = matchingCategory;
    }

    /** Builder용 생성자 : 서브클래스에서 호출 */
    protected RedisBoard(UUID userUUID,
                         String memo,
                         MatchingCategory matchingCategory) {
        this(UUID.randomUUID(), userUUID, memo, matchingCategory);
    }

    // Duo 게시판용 서브클래스
    @Getter
    public static class Duo extends RedisBoard {
        private DuoMapCode duoMapCode;

        /** Redis 조회 시 사용할 생성자 */
        @PersistenceCreator
        protected Duo(UUID uuid,
                      UUID userUUID,
                      String memo,
                      MatchingCategory matchingCategory,
                      DuoMapCode duoMapCode) {
            super(uuid, userUUID, memo, matchingCategory);
            this.duoMapCode = duoMapCode;
        }

        /** 빌더용 생성자 : 신규 게시글 생성 시 */
        @Builder
        public Duo(UUID userUUID,
                   String memo,
                   DuoMapCode duoMapCode) {
            super(userUUID, memo, MatchingCategory.Duo);
            this.duoMapCode = duoMapCode;
        }
    }

    // Colosseum 게시판용 서브클래스
    @Getter
    public static class Colosseum extends RedisBoard {
        private ColosseumMapCode mapCode;
        private Integer          headCount;

        @PersistenceCreator
        protected Colosseum(UUID uuid,
                            UUID userUUID,
                            String memo,
                            MatchingCategory matchingCategory,
                            ColosseumMapCode mapCode,
                            Integer headCount) {
            super(uuid, userUUID, memo, matchingCategory);
            this.mapCode   = mapCode;
            this.headCount = headCount;
        }

        @Builder
        public Colosseum(UUID userUUID,
                         String memo,
                         ColosseumMapCode mapCode,
                         Integer headCount) {
            super(userUUID, memo, MatchingCategory.Colosseum);
            this.mapCode   = mapCode;
            this.headCount = headCount;
        }
    }
}