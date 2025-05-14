package com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "MyPage", timeToLive = 15 * 60)
@Getter @Builder @AllArgsConstructor
public class RedisMyPage {
    @Id
    private final UUID uuid;

    @Indexed
    private MatchingStatus status;

    private UUID boardUUID;

    /** 레디스 전용 내부 인덱스 필드 */
    @Indexed
    private final MatchingCategory matchingCategory;

    @Indexed
    private Long acceptorId;

    @Indexed
    private Long requestorId;


}
