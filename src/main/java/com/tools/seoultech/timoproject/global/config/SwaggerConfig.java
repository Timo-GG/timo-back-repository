package com.tools.seoultech.timoproject.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


import lombok.RequiredArgsConstructor;
import org.springdoc.core.configuration.SpringDocDataRestConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 인증이 필요없는 API에는 @Operation(summary = "공개 API", security = @SecurityRequirement(name = "")) 추가.
*/

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "API 문서",
                version = "v1",
                description = "swagger v1 UI Test"
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
                .components(new Components());
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"/api/v1/**"};
        String[] packagesToScan = {
                "com.tools.seoultech.timoproject.riot",
                "com.tools.seoultech.timoproject.memberAccount",
                "com.tools.seoultech.timoproject.matching.controller",
                "com.tools.seoultech.timoproject.ranking",
                "com.tools.seoultech.timoproject.auth",
                "com.tools.seoultech.timoproject.riot",
                "com.tools.seoultech.timoproject.notification",

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
