package com.tools.seoultech.timoproject;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SpringDocDataRestConfiguration.class)
@EnableRedisDocumentRepositories(basePackages = {
//        "com.tools.seoultech.timoproject.matching.domain.board.entity.redis",
//        "com.tools.seoultech.timoproject.matching.domain.user.entity.redis",
//        "com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis",
//        "com.tools.seoultech.timoproject.ranking",
//        "com.redis.om.documents.*"
        "com.tools.seoultech.timoproject.*"
})
public class TimoProjectApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TimoProjectApplication.class, args);
    }

    @Value("${spring.data.redis.host}")
    String redisProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> Redis host: " + redisProperties);
    }
}