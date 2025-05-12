package com.tools.seoultech.timoproject;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JedisTest {

    @Test
    public void testJedisConnection() {
        // Jedis 클라이언트를 이용한 Redis 연결
        Jedis jedis = new Jedis("localhost", 6379);

        // Redis에 값 저장
        jedis.set("testKey", "testValue");

        // Redis에서 값 조회
        String value = jedis.get("testKey");

        // 값을 검증
        assertEquals("testValue", value);

        // 연결 종료
        jedis.close();
    }
}