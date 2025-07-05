package com.tools.seoultech.timoproject.riot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class ChampionCacheService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Getter
    private final Map<Integer, String> championIdToNameMap = new HashMap<>();

    private static final String CHAMPION_VERSION = "14.23.1"; // 또는 최신 버전 관리
    private static final String BASE_URL = "https://ddragon.leagueoflegends.com/cdn/";

    @PostConstruct
    public void init() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(BASE_URL)
                    .append(CHAMPION_VERSION)
                    .append("/data/en_US/champion.json");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(sb.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            JsonNode data = root.get("data");

            Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String name = entry.getValue().get("id").asText();
                int key = Integer.parseInt(entry.getValue().get("key").asText());
                championIdToNameMap.put(key, name);
            }

            log.info("✅ 챔피언 캐시 로딩 완료: {}개", championIdToNameMap.size());

        } catch (Exception e) {
            log.error("❌ 챔피언 JSON 캐싱 실패", e);
        }
    }
}
