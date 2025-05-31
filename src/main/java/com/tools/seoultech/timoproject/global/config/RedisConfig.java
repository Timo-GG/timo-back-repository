package com.tools.seoultech.timoproject.global.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(
        enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP
)
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

    @PostConstruct
    public void configureRedis() {
        try {
            RedisTemplate<String, Object> template = redisTemplate();
            template.execute((RedisCallback<Object>) connection -> {
                connection.execute("CONFIG", "SET".getBytes(), "notify-keyspace-events".getBytes(), "Ex".getBytes());
                return null;
            });
        } catch (Exception e) {
            // Redis 설정 실패 시 로그만 출력
            System.out.println("Redis keyspace events 설정 실패: " + e.getMessage());
        }
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
