package com.tools.seoultech.timoproject.ranking;

import com.tools.seoultech.timoproject.member.MemberRepository;
import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.domain.entity.Member;
import com.tools.seoultech.timoproject.member.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.member.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisDataSeeder {

    private final MemberRepository memberRepository;
    private final RankingFacade rankingFacade;

    /**
     * 시딩 실행 메서드 (컨트롤러 등에서 호출)
     */
    public void seedAll() {
        List<Record> accounts = List.of(
                new Record("mBSOaEQec9_RqMMaNziBfMnnRJDghKxjt7LkWtR7XCVvmxNOJvfeoh2lK0CQv7gyKUoLpYmTTXT61Q", "Grizzly"),
                new Record("qSSkjTj-tHb_ESbnRcgglbAg4leEq8wkZJ704dIlP1xaz0oJGxBl5tLLKsIFJ3hOFZll7HyKHNnalg", "젠레스존제로우"),
                new Record("iuVfUuyrpOSAXcvaER6avOXVBNg2X9TpatbWNU6clYzbbyCR5TiObI8sg03-ObxkgjxVu4-y-UnsAQ", "화이팅가보자"),
                new Record("g6w3J6m5mdVlrqMXS4mrzPjkfEb_uEBNHO1BsX8Th99djLTC9qu69zNVbMcYz1-kKdN2JfG2sATzAA", "질병게임1"),
                new Record("a0Cusd6WwwfNbZIOhR0ubLQ-C7FWGpDuaNybhiKJr4XgLpaK3Wl1twwq1xRModHCrLGVukAmHifexg", "gaubeo202"),
                new Record("y2Pm33b7YvZiUN4FIbbrZBhu3sHN8xyGwLMetbvVuiRIJYIXMButZItv8R2pKeQMK4td6g_qPWTqXQ", "델리델리델리빵"),
                new Record("6_F8VCBuYAQZhJ8HxOV6Buri54Dnv0z0XbLwkN8iOWTTYhN87HWuAmYXUDQizmfEpPkJTT1_O3cRxA", "RlwjD"),
                new Record("x5TQOacbARKLbznMY1oqWzGVTZhBb-OLozfiMCa9McPKLJ7gO6tUhm6oZy4ffd_lwYqzRS--uxybFQ", "라쿤99")
        );

        String[] universities = {
                "서울과학기술대학교",
                "연세대학교",
                "고려대학교",
                "한양대학교",
                "서울과학기술대학교",
                "동국대학교",
                "서울과학기술대학교",
        };

        for (int i = 0; i < accounts.size(); i++) {
            String username = "user" + (i + 1);
            if (memberRepository.existsByUsername(username)) {
                log.info("Skip seeding for {}: already exists", username);
                continue;
            }

            Record r = accounts.get(i);

            // 랜덤 대학 및 학과 선택
            String university = universities[i % universities.length];
            String univEmail = "test" + i + "@" + university.replace(" ", "").toLowerCase() + ".ac.kr";

            Member account = Member.builder()
                    .email(username + i + "@example.com")
                    .username(username)
                    .riotAccount(new RiotAccount(r.puuid(), r.gameName(), "KR1", "iconUrl"))
                    .certifiedUnivInfo(new CertifiedUnivInfo(univEmail, university))
                    .oAuthProvider(OAuthProvider.KAKAO)
                    .role(Role.MEMBER)
                    .build();

            account = memberRepository.save(account);
            rankingFacade.createRanking(account.getMemberId(), r.puuid());
            log.info("Seeded and ranked {} at {}", username, university);
        }

        log.info("✅ Redis seeding complete: {} accounts processed.", accounts.size());
    }

    private record Record(String puuid, String gameName) {
    }
}