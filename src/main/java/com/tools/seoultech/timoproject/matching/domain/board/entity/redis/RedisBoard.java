package com.tools.seoultech.timoproject.matching.domain.board.entity.redis;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.redis.om.spring.annotations.Searchable;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.ColosseumMapCode;
import com.tools.seoultech.timoproject.matching.domain.board.entity.enumType.DuoMapCode;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "matchingCategory",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisBoard.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisBoard.Colosseum.class, name = "COLOSSEUM")
})
public abstract class RedisBoard {
    @Document(timeToLive = 15 * 60)
    @Schema(description = "DuoBoard")
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Duo extends RedisBoard {
        @Id
        private String uuid;

        @Reference @Searchable
        private RedisUser redisUser;

        private String memo;

        @Indexed @Searchable
        private MatchingCategory matchingCategory;
        private DuoMapCode mapCode;
    }

    // Colosseum 게시판용 서브클래스
    @Document(timeToLive = 15 * 60)
    @Schema(description = "Colosseum Board")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Colosseum extends RedisBoard {
        @Id
        private String uuid;

        @Reference @Searchable
        private RedisUser redisUser;

        private String memo;

        @Indexed @Searchable
        private MatchingCategory matchingCategory;

        private ColosseumMapCode mapCode;
        private Integer          headCount;
    }
}
