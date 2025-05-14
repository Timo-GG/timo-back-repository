package com.tools.seoultech.timoproject.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableJpaRepositories(basePackages = "com.tools.seoultech.timoproject.*")
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // NOTE: RedisTemplate용 Config
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /** Spring <-> Redis 간에 전송 형태 설정 : Sorted-Set 및 Hash 데이터 처리를 위한 RedisTemplate 설정 */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // Key를 String으로 저장
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); // 이거 추가!

        // Value를 JSON 형식으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    /** 게시판 관련 RedisTemplate 설정 */
    @Bean(name = "boardRedisTemplate")
    public RedisTemplate<String, RedisBoard> boardRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisBoard> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /** 사용자 관련 RedisTemplate 설정 */
    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, RedisUser> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisUser> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
    /** 마이페이지 관련 RedisTemplate 설정 */
    @Bean(name = "myPageRedisTemplate")
    public RedisTemplate<String, RedisMyPage> myPageRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisMyPage> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /** ZSetOperations Bean 추가 */
    @Bean
    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /** ZSET용 + 해시용 통합 RedisTemplate */
    @Bean(name = "rankingRedisTemplate")
    public RedisTemplate<String, Object> rankingRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // ✅ ZSET value: memberId (String)
        template.setValueSerializer(new StringRedisSerializer());

        // ✅ HASH value: 복잡한 JSON 객체
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}
