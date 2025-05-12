package com.tools.seoultech.timoproject.global.config;


import com.redis.om.spring.RedisModulesConfiguration;
import com.tools.seoultech.timoproject.matching.domain.board.entity.redis.RedisBoard;
import com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis.RedisMyPage;
import com.tools.seoultech.timoproject.matching.domain.user.entity.redis.RedisUser;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;
//
//    @Bean
//    public LettuceConnectionFactory lettuceConnectionFactory() {
//        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
//    }

    // 2. Jedis (Redis OM 전용)
//    @Primary
    @Bean
//    @Qualifier("jedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    // RedisCommands Bean for direct connection to Redis
//    @Bean
//    public RedisCommands<String, String> redisCommands(JedisConnectionFactory factory) {
//        RedisClient client = RedisClient.create("redis://localhost:6379"); // Make sure to match your Redis config
//        StatefulRedisConnection<String, String> connection = client.connect();
//        return connection.sync();
//    }
    @Bean
    public JedisCommands jedisCommands(JedisConnectionFactory jedisConnectionFactory) {
        // Jedis 클라이언트 연결
        Jedis jedis = (Jedis) jedisConnectionFactory.getConnection().getNativeConnection();
        return jedis; // JedisCommands 인터페이스를 반환
    }

    /** Spring <-> Redis 간에 전송 형태 설정 : Sorted-Set 및 Hash 데이터 처리를 위한 RedisTemplate 설정 */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // Key를 String으로 저장
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

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
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value를 JSON 형식으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, RedisUser> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisUser> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value를 JSON 형식으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    /** 마이페이지 관련 RedisTemplate 설정 */
    @Bean(name = "myPageRedisTemplate")
    public RedisTemplate<String, RedisMyPage> myPageRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, RedisMyPage> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value를 JSON 형식으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
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
    @Bean
    public RedisModulesConfiguration redisModulesConfiguration() {
        return new RedisModulesConfiguration();
    }
}
