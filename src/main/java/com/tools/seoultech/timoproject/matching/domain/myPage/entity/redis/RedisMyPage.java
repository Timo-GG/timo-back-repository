package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.redis.om.spring.annotations.Document;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;


import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.*;

import java.util.UUID;

//@RedisHash(value = "MyPage", timeToLive = 15 * 60)
@Document
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "matchingCategory")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RedisBoard.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisBoard.Colosseum.class, name = "COLOSSEUM"),
        @JsonSubTypes.Type(value = RedisUser.Duo.class, name = "DUO"),
        @JsonSubTypes.Type(value = RedisUser.Colosseum.class, name = "COLOSSEUM")
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedisMyPage {
    @Id
    private String uuid;

    @Indexed
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private MatchingCategory matchingCategory;

    @Indexed
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private MatchingStatus status;

    @Reference @Indexed
    @Schema(type = "object", oneOf = { RedisBoard.Duo.class, RedisBoard.Colosseum.class})
    private RedisBoard board;

    @Reference @Indexed
    @Schema(type = "object", oneOf = { RedisUser.Duo.class, RedisUser.Colosseum.class})
    private RedisUser requestor;

    @Indexed private Long acceptorMemberId;
    @Indexed private Long requestorMemberId;
}
