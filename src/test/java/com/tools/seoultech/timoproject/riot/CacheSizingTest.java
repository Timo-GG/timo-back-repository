package com.tools.seoultech.timoproject.riot;

import com.tools.seoultech.timoproject.riot.dto.MatchSummaryDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@Slf4j
class CacheSizingTest {

    // 객체의 대략적인 바이트 크기를 측정하는 헬퍼 메소드
    private long getObjectSize(Object obj) {
        if (obj == null) return 0;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            return baos.size();
        } catch (Exception e) {
            log.error("객체 크기 측정 실패", e);
            return -1;
        }
    }

    @Test
    @DisplayName("MatchSummaryDTO 객체 하나의 예상 메모리 크기를 측정한다")
    void measureMatchSummaryDTOSize() {
        // given: 실제 데이터와 유사한 길이의 더미 데이터로 객체 생성
        MatchSummaryDTO dto = new MatchSummaryDTO(
                "15분 30초",
                "어제",
                "솔로랭크",
                "Hide on bush",
                "https://ddragon.leagueoflegends.com/cdn/img/champion/tiles/Faker_0.jpg",
                18, 10, 2, 15, true, "9.5 CS/분",
                List.of("rune_url1", "rune_url2"),
                List.of("spell_url1", "spell_url2"),
                List.of("item_url1", "item_url2", "item_url3", "item_url4", "item_url5", "item_url6", "item_url7")
        );

        // when: 객체 크기 측정
        long sizeInBytes = getObjectSize(dto);

        // then: 결과 출력
        log.info("MatchSummaryDTO 1개 객체의 예상 크기: {} bytes (약 {} KB)", sizeInBytes, String.format("%.2f", sizeInBytes / 1024.0));

        // then: 100MB 힙 메모리에 몇 개나 저장 가능한지 예측
        if (sizeInBytes > 0) {
            long cacheSizeInMB = 100;
            long totalCacheBytes = 1024 * 1024 * cacheSizeInMB;
            long estimatedCount = totalCacheBytes / sizeInBytes;
            log.info("100MB 캐시 메모리에 약 {}개의 MatchSummaryDTO를 저장할 수 있습니다.", String.format("%,d", estimatedCount));
        }
    }
}