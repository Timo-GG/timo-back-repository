package com.tools.seoultech.timoproject.firebase;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMTokenService {

    private final FCMTokenRepository fcmTokenRepository;

    @Transactional
    public void deactivateTokens(List<String> tokens) {
        if (!tokens.isEmpty()) {
            fcmTokenRepository.deactivateTokensByTokenValues(tokens);
            log.info("{}개의 FCM 토큰을 비활성화했습니다", tokens.size());
        }
    }
}
