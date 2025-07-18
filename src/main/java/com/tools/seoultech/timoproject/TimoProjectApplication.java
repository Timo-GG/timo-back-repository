package com.tools.seoultech.timoproject;

import jakarta.annotation.PostConstruct;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication(exclude = SpringDocDataRestConfiguration.class)
@EnableCaching
public class TimoProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(TimoProjectApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // JVM 타임존 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        // 시스템 프로퍼티로도 설정
        System.setProperty("user.timezone", "Asia/Seoul");
    }
}