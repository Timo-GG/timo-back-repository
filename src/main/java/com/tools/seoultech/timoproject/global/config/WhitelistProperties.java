package com.tools.seoultech.timoproject.global.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "security.whitelist")
public class WhitelistProperties {
    private List<String> urls;
    private List<String> publicUrls;
}