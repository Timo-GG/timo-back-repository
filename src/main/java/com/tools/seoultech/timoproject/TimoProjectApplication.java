package com.tools.seoultech.timoproject;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude = SpringDocDataRestConfiguration.class)
@EnableRedisDocumentRepositories(basePackages = {
        "com.tools.seoultech.timoproject.matching.domain.board.entity.redis",
        "com.tools.seoultech.timoproject.matching.domain.user.entity.redis",
        "com.tools.seoultech.timoproject.matching.domain.myPage.entity.redis",
        "com.tools.seoultech.timoproject.ranking",
        "com.redis.om.documents.*"
})
public class TimoProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimoProjectApplication.class, args);
    }
}