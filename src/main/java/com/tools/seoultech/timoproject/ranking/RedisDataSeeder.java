package com.tools.seoultech.timoproject.ranking;

import com.tools.seoultech.timoproject.memberAccount.domain.OAuthProvider;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.enumType.Role;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.memberAccount.domain.entity.embeddableType.RiotAccount;
import com.tools.seoultech.timoproject.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.ranking.facade.RankingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class RedisDataSeeder {

    private final MemberAccountRepository memberAccountRepository;
    private final RankingFacade rankingFacade;

    /**
     * 시딩 실행 메서드 (컨트롤러 등에서 호출)
     */
    public void seedAll() {
        List<Record> accounts = List.of(
                new Record("_PKwQ67bodvzelczCj5fuTf2Z9kuTcAfbv4HMkqrkPZGds5byukttzJFh_j_q2ixAnNE1_76R_lwAA", "Grizzly"),
                new Record("Pk7D_JYdzK75BDx-Z4v38LxzoE9ZabqEu2I0nUlcQ-hwZO6NLVni1h_xYPZiiOYs8xVESXHMFDWhUw", "젠레스존제로우"),
                new Record("qMVH5avv7uIks_HsABEXNcu19y5PTU6wUZw4lNceovcgu5OoR3MHgffp5dz6HKzJNiRZaznHQ_SMbg", "화이팅가보자"),
                new Record("RnNIWfIB7GzlUz0Ce3AfgId6KnfLngVoGFO3XanbjVdAAN9I734yX3M1GjU2K9YYO-Wy3lHs0cG21A", "상욱Ryu"),
                new Record("VgmEzR-sBudWE0n5WFVkbWjNQ-LaguyVFG1nqUAQq1sgp-zZ5_kVueYCKIQE88Rc9wpKATVyotnlkQ", "gaubeo202"),
                new Record("ek6sEt_WkYH_b8dmGIQ0BJ8bqFQLqSNY5VS45KUpX9It2lqNgVgy47AT1hZ1MPrg0iWqQij4oEQK8g", "델리델리델리빵"),
                new Record("kj6WEamVjBIUvNQ9bDYntsIGWDZmVwO6pzWnuQ-6ooKggrESnctOHJDkK9JC8LYn7FAJ0J0BeXGDMg", "RlwjD"),
                new Record("2dktmBwMUaHn7bQR6JsHbv6pk51eTPve50ngq_y-_JlWpX_-xkS9UohgQsEGi4MouEgNlEp-pJMZnQ", "라쿤99")
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
            if (memberAccountRepository.existsByUsername(username)) {
                log.info("Skip seeding for {}: already exists", username);
                continue;
            }

            Record r = accounts.get(i);

            // 랜덤 대학 및 학과 선택
            String university = universities[i % universities.length];
            String univEmail = "test" + i + "@" + university.replace(" ", "").toLowerCase() + ".ac.kr";

            MemberAccount account = MemberAccount.builder()
                    .email(username + i + "@example.com")
                    .username(username)
                    .riotAccount(new RiotAccount(r.puuid(), r.gameName(), "KR1", "iconUrl"))
                    .certifiedUnivInfo(new CertifiedUnivInfo(univEmail, university))
                    .oAuthProvider(OAuthProvider.KAKAO)
                    .role(Role.MEMBER)
                    .build();

            account = memberAccountRepository.save(account);
            rankingFacade.createRanking(account.getMemberId(), r.puuid());
            log.info("Seeded and ranked {} at {}", username, university);
        }

        log.info("✅ Redis seeding complete: {} accounts processed.", accounts.size());
    }

    private record Record(String puuid, String gameName) {
    }
}