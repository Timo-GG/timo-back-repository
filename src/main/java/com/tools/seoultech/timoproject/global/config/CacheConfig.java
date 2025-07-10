package com.tools.seoultech.timoproject.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        // 룬 데이터 캐시 (24시간)
        cacheManager.registerCustomCache("runeData", Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());

        // 매치 상세 정보 캐시 (최대 10만개, 1시간)
        cacheManager.registerCustomCache("matches", Caffeine.newBuilder()
                .maximumSize(100_000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build());

        // 랭크 정보 캐시 (최대 1만개, 10분) - 랭크는 변할 수 있으므로 만료시간을 짧게 설정
        cacheManager.registerCustomCache("rankInfo", Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build());

        // 프로필 아이콘 캐시 (최대 1만개, 24시간) - 아이콘은 거의 변하지 않음
        cacheManager.registerCustomCache("profileIcons", Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());

        // 계정 정보(PUUID) 캐시 (최대 1만개, 24시간) - PUUID는 거의 변하지 않음
        cacheManager.registerCustomCache("accounts", Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build());

        // 매치 목록 캐시 (최대 1만개, 10분) - 게임을 하면 바로 바뀌므로 만료시간을 짧게 설정
        cacheManager.registerCustomCache("matchLists", Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build());

        return cacheManager;
    }
}
