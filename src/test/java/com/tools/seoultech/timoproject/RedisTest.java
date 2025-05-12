package com.tools.seoultech.timoproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisConnection() {
        // Redis에 값 저장
        redisTemplate.opsForValue().set("testKey", "testValue");

        // Redis에서 값 조회
        String value = redisTemplate.opsForValue().get("testKey");

        // 값을 검증
        assertEquals("testValue", value);
    }
}
