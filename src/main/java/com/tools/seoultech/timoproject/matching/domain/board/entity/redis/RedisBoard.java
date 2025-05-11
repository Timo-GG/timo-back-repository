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
import org.springframework.data.annotation.PersistenceCreator;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;

import java.util.UUID;

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
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class RedisBoard {
    @Id
    private String uuid;

    @Reference @Searchable
    private RedisUser redisUser;

    private String memo;

    @Indexed
    // 서버 내부 필드. 역직렬화에 사용되지 않음. 직렬화에서는 편의성 때문에 보여주긴 함.
    private MatchingCategory matchingCategory;

    // Duo 게시판용 서브클래스
    @Schema(description = "DuoBoard")
    @Document
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Duo extends RedisBoard {
        private DuoMapCode mapCode;

        @Builder
        public Duo(String uuid, RedisUser redisUser, String memo, MatchingCategory matchingCategory, DuoMapCode mapCode ){
            super(uuid, redisUser, memo, matchingCategory);
            this.mapCode = mapCode;
        }
    }

    // Colosseum 게시판용 서브클래스
    @Schema(description = "Colosseum Board")
    @Document
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Colosseum extends RedisBoard {
        private ColosseumMapCode mapCode;
        private Integer          headCount;

        @Builder
        public Colosseum(String uuid, RedisUser redisUser, String memo, MatchingCategory matchingCategory, Integer headCount){
            super(uuid, redisUser, memo, matchingCategory);
            this.mapCode = null;
        }
    }
}
