package com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity;

import com.tools.seoultech.timoproject.global.BaseEntity;
import com.tools.seoultech.timoproject.version2.matching.domain.myPage.entity.EnumType.MatchingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("MyPageBoard")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Redis_MyPageBoard extends BaseEntity {
    @Id
    private Long myPage_id;

    private MatchingStatus matchingStatus;

    private Long redisRequestorId;
    private Long redisAcceptorId;
}
