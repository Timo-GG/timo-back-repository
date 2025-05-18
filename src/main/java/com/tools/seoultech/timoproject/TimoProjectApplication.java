package com.tools.seoultech.timoproject;

import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(exclude = SpringDocDataRestConfiguration.class)
public class TimoProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimoProjectApplication.class, args);
    }
}