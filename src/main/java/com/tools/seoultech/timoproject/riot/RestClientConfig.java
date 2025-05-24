package com.tools.seoultech.timoproject.riot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class RestClientConfig {

    @Value("${api_key}")
    private String apiKey;

    @Bean
    public ExecutorService matchInfoExecutor() {
        return Executors.newFixedThreadPool(10); // 매치 10개 동시 처리
    }

    // RestClient 설정 (동기식 - 안정성 우선)
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(new SimpleClientHttpRequestFactory())
                .defaultHeader("X-Riot-Token", apiKey)
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build();
    }

    // 동기식 클라이언트들
    @Bean
    public RiotAsiaApiClient riotAsiaApiClient(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(RiotAsiaApiClient.class);
    }

    @Bean
    public RiotKrApiClient riotKrApiClient(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(RiotKrApiClient.class);
    }

    @Bean
    public DataDragonClient dataDragonClient(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();
        return factory.createClient(DataDragonClient.class);
    }

}
