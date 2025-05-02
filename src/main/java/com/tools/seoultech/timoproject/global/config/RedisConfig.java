package com.tools.seoultech.timoproject.global.config;

import com.tools.seoultech.timoproject.version2.matching.domain.board.entity.redis.RedisBoardDTO;
import com.tools.seoultech.timoproject.version2.matching.domain.user.entity.redis.RedisUserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
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

        // Value를 JSON 형식으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }

    @Bean(name = "boardRedisTemplate")
    public RedisTemplate<String, RedisBoardDTO<?>> boardRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisBoardDTO<?>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, RedisUserDTO<?>> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisUserDTO<?>> template = new RedisTemplate<>();
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
}
