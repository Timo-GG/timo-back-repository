package com.tools.seoultech.timoproject.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

    /** RestTemplate 을 사용하기 위해 Spring Bean 컴포넌트로 등록 */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}