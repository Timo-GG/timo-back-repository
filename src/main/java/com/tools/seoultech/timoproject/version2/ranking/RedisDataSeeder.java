package com.tools.seoultech.timoproject.version2.ranking;

import com.tools.seoultech.timoproject.member.domain.OAuthProvider;
import com.tools.seoultech.timoproject.member.domain.Role;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.CertifiedUnivInfo;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.MemberAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.domain.entity.RiotAccount;
import com.tools.seoultech.timoproject.version2.memberAccount.MemberAccountRepository;
import com.tools.seoultech.timoproject.version2.ranking.facade.RankingFacade;
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
                new Record("BubdLM2BnNUArdnJgV7necWTPcEwNzOuRvUIR8_hf1so6fqanFExU5uVX5dnzCZD0dY3u82Bb6XDzw", "빌런노"),
                new Record("KRck107g8bjdNl5Gijw2g-uY9AV1m2trhaSjl5OIMPeqQY-4FxhYQknrCkDul8MN21_aY1ew7TaDZQ", "WXD"),
                new Record("HEhaF1ymbhZtaETj4xY5hXNT6hMu1RvwICvdey9o08Kjvmuyt0eYH5XwFMMSZhF6u54GZ5FJOdqP2g", "매력적인 닭"),
                new Record("m3o0FUiBjjMmqX7-bMZFRzGewooBHhN6aj333bEGkXMuCyssOAvR56x07JyORW8Sv-8tq-M4vT9Mww", "도구야정신좀차려"),
                new Record("7B9KnS5HSZOYoLN67yeVnnB65lgfCWe5DoTWMYMbWhtAsmX-sUu4pDYo-Yv-D_skyInq9WjTQQcyIg", "귀 염"),
                new Record("eFLOP2cvyxtg9Ig3kF-0tlu8Ijo5HobweVSmZPWFrm_2GBFj72Rs5C19Qbz6H_mA1RouMlAsWLCnBA", "으댜다"),
                new Record("0xCXf-g6dYIBAtfV_eJtfZSqX_gQtUmd5AxSAoohvaAXOP_jEELCBkwC3RpoB2tGNN4iKa8RLeH_g", "Cuzz")
        );

        for (int i = 0; i < accounts.size(); i++) {
            String username = "user" + (i + 1);
            if (memberAccountRepository.existsByUsername(username)) {
                log.info("Skip seeding for {}: already exists", username);
                continue;
            }
            Record r = accounts.get(i);

            MemberAccount account = MemberAccount.builder()
                    .email(username+i + "@example.com")
                    .username(username)
                    .riotAccount(new RiotAccount(r.puuid(), r.gameName(), "KR1"))
                    .certifiedUnivInfo(new CertifiedUnivInfo("test" + i +  "@school.ac.kr", "서울과학기술대학교"))
                    .oAuthProvider(OAuthProvider.KAKAO)
                    .role(Role.MEMBER)
                    .build();

            account = memberAccountRepository.save(account);
            rankingFacade.createRanking(account.getMemberId(), r.puuid());
            log.info("Seeded and ranked {}", username);
        }

        log.info("✅ Redis seeding complete: {} accounts processed.", accounts.size());
    }

    private record Record(String puuid, String gameName) {}
}