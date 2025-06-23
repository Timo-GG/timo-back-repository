package com.tools.seoultech.timoproject.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

@Configuration
public class FirebaseConfig {

    private FirebaseApp firebaseApp;

    @PostConstruct
    public FirebaseApp initializeFcm() throws IOException {
        String base64EncodedKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");

        System.out.println("=== Firebase 초기화 디버깅 ===");
        System.out.println("FIREBASE_SERVICE_ACCOUNT_KEY exists: " + (base64EncodedKey != null));

        if (base64EncodedKey != null && !base64EncodedKey.trim().isEmpty()) {
            try {
                System.out.println("Firebase 초기화: Base64 환경변수 사용");
                System.out.println("Key length: " + base64EncodedKey.length());

                // Base64 디코딩
                byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedKey.trim());
                String decodedJson = new String(decodedBytes);

                System.out.println("디코딩된 JSON 길이: " + decodedJson.length());
                System.out.println("JSON 시작 부분: " + decodedJson.substring(0, Math.min(100, decodedJson.length())));

                // JSON 유효성 검사
                if (!decodedJson.startsWith("{") || !decodedJson.contains("private_key")) {
                    throw new RuntimeException("유효하지 않은 JSON 형식");
                }

                // JSON을 Map으로 파싱한 후 다시 InputStream으로 변환
                ObjectMapper mapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, Object> serviceAccount = mapper.readValue(decodedJson, Map.class);

                // Map을 다시 JSON 문자열로 변환
                String jsonString = mapper.writeValueAsString(serviceAccount);
                InputStream credentialsStream = new ByteArrayInputStream(jsonString.getBytes());

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase 초기화 성공! (Base64)");

            } catch (Exception e) {
                System.err.println("Base64 디코딩 또는 Firebase 초기화 실패: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Firebase 초기화 실패", e);
            }
        } else {
            try {
                // GOOGLE_APPLICATION_CREDENTIALS 환경변수 방식
                System.out.println("Firebase 초기화: GOOGLE_APPLICATION_CREDENTIALS 사용");
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase 초기화 성공! (Application Default)");
            } catch (IOException e) {
                // 클래스패스에서 직접 읽기 (로컬 개발용 fallback)
                System.out.println("환경변수 방식 실패, 클래스패스에서 직접 읽기 시도...");
                try {
                    ClassPathResource resource = new ClassPathResource("config/firebase-service-account.json");
                    InputStream serviceAccount = resource.getInputStream();

                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    firebaseApp = FirebaseApp.initializeApp(options);
                    System.out.println("Firebase 초기화 성공! (ClassPath)");
                } catch (IOException fallbackException) {
                    System.err.println("Firebase 초기화 완전 실패!");
                    System.err.println("1. FIREBASE_SERVICE_ACCOUNT_KEY 환경변수 확인");
                    System.err.println("2. GOOGLE_APPLICATION_CREDENTIALS 환경변수 확인");
                    System.err.println("3. classpath:config/firebase-service-account.json 파일 확인");
                    throw new RuntimeException("Firebase 초기화 실패", fallbackException);
                }
            }
        }

        return firebaseApp;
    }

    @Bean
    public FirebaseMessaging initFirebaseMessaging() {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
