package com.tools.seoultech.timoproject.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

/**
 * 인증이 필요없는 API에는 @Operation(summary = "공개 API", security = @SecurityRequirement(name = "")) 추가.
*/

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "TIMO.GG API",
                version = "v1",
                description = "대학생 전용 LOL 매칭 플랫폼 API"
        ),
        security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
)
@SecurityScheme(
        name = SwaggerConfig.SECURITY_SCHEME_NAME,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    public static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TIMO.GG API")
                        .version("v1")
                        .description("대학생 전용 LOL 매칭 플랫폼 API"))
                .servers(List.of(
                        new Server().url("https://api.timo.kr").description("Production server"),
                        new Server().url("http://localhost:8080").description("Local development server")
                ))
                .components(new Components());
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/api/v1/**"};
        String[] packagesToScan = {
                "com.tools.seoultech.timoproject.member",
                "com.tools.seoultech.timoproject.matching.controller",
                "com.tools.seoultech.timoproject.ranking",
                "com.tools.seoultech.timoproject.auth",
                "com.tools.seoultech.timoproject.riot",
                "com.tools.seoultech.timoproject.notification",
                "com.tools.seoultech.timoproject.review"

        };
        return GroupedOpenApi.builder()
                .group("springdoc-openapi")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }

    /**
     * springdoc-openapi의 Data REST 통합 비활성화용 빈
     */
    @Bean
    public SpringDocDataRestConfiguration springDocDataRestConfiguration() {
        return new SpringDocDataRestConfiguration();
    }

    @Bean
    public SpringDocConfigProperties springDocConfigProperties() {
        SpringDocConfigProperties props = new SpringDocConfigProperties();
        props.getApiDocs().setEnabled(false);
        return props;
    }
}
