package com.tools.seoultech.timoproject.firebase;

import com.tools.seoultech.timoproject.global.annotation.CurrentMemberId;
import com.tools.seoultech.timoproject.riot.dto.APIDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fcm")
@RequiredArgsConstructor
@Slf4j
public class FCMController {

    private final FCMTokenRepository fcmTokenRepository;

    @PostMapping("/token")
    @Transactional
    public APIDataResponse<String> saveToken(@CurrentMemberId Long memberId,
                                             @RequestBody TokenRequest request) {
        // 기존 토큰이 있는지 확인
        fcmTokenRepository.findByMemberIdAndToken(memberId, request.getToken())
                .ifPresentOrElse(
                        existingToken -> {
                            existingToken.setActive(true);
                            fcmTokenRepository.save(existingToken);
                            log.info("기존 FCM 토큰 활성화: memberId={}", memberId);
                        },
                        () -> {
                            FCMToken newToken = new FCMToken(memberId, request.getToken(), "WEB");
                            fcmTokenRepository.save(newToken);
                            log.info("새 FCM 토큰 저장: memberId={}", memberId);
                        }
                );

        return APIDataResponse.of("FCM 토큰이 저장되었습니다.");
    }

    @DeleteMapping("/token")
    @Transactional
    public APIDataResponse<String> deleteToken(@CurrentMemberId Long memberId,
                                               @RequestBody TokenRequest request) {
        fcmTokenRepository.deactivateToken(memberId, request.getToken());
        log.info("FCM 토큰 비활성화: memberId={}", memberId);
        return APIDataResponse.of("FCM 토큰이 비활성화되었습니다.");
    }

    public static class TokenRequest {
        private String token;

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
