package com.tools.seoultech.timoproject.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** @EnableJpaAuditing: DB 설정을 바꾸면서 오류가 발생했는데, EnableJpaAuditing을 application 클래스에 설정하면
SpringBootTest 통합 테스트가 아닌 WebMVC와 같은 단위 테스트환경에서 JpaAuditing이 적용되지 않아
context 오류가 발생. 따라서 application에 적용하지 않고 configuration에 적용하여 스프링부터 실행전 configuration 설정단계로 넘김.
 */

@Configuration
@EnableJpaAuditing
@EnableWebMvc
@RequiredArgsConstructor
public class QueryDSLConfig implements WebMvcConfigurer {
    private final EntityManager em;

    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
