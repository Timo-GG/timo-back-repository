package com.tools.seoultech.timoproject.matching.domain.myPage.dto;

import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingCategory;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.EnumType.MatchingStatus;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import lombok.Builder;

import java.util.UUID;

// Note: dto별로 존재하는 Request, Response를 어노테이션으로 메타데이터를 만들면 어떨까?
public abstract class MyPageDTO {
    // Note: 1. 검색용
    @Builder
    public record RequestSearch(
            UUID myPageUUID,
            MatchingCategory matchingCategory,
            Boolean isRequestor,
            MatchingStatus status
    ){}
    // Note: 2. 검색 결과 Response
    public record ResponseDuo(
            UUID mypageUUID,
            MatchingCategory matchingCategory,
            RedisMyPage redisMyPage,
            RedisUser.Duo requestor,
            RedisUser.Duo acceptor
    ){}
    public record ResponseColosseum(
            UUID mypageUUID,
            MatchingCategory matchingCategory,
            RedisMyPage redisMyPage,
            RedisUser.Duo requestor,
            RedisUser.Duo acceptor
    ){}
    public record ResponseMyPage(
            UUID myPageUUID,
            MatchingCategory matchingCategory,
            RedisUser requestor,
            RedisUser acceptor,
            MatchingStatus status
    ){}
}
