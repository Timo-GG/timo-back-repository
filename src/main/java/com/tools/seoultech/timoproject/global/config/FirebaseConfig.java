package com.tools.seoultech.timoproject.global.config;

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

@Configuration
public class FirebaseConfig {

    private FirebaseApp firebaseApp;

    @PostConstruct
    public FirebaseApp initializeFcm() throws IOException {
        String base64EncodedKey = System.getenv("FIREBASE_SERVICE_ACCOUNT_KEY");

        if (base64EncodedKey != null) {
            // BASE64 방식
            InputStream credentialsStream = new ByteArrayInputStream(
                    Base64.getDecoder().decode(base64EncodedKey)
            );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .build();

            firebaseApp = FirebaseApp.initializeApp(options);
        } else {
            try {
                // 환경변수 방식 시도
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                // 클래스패스에서 직접 읽기 (fallback)
                System.out.println("환경변수 방식 실패, 클래스패스에서 직접 읽기 시도...");
                ClassPathResource resource = new ClassPathResource("config/firebase-service-account.json");
                InputStream serviceAccount = resource.getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
                System.out.println("Firebase 초기화 성공!");
            }
        }

        return firebaseApp;
    }

    @Bean
    public FirebaseMessaging initFirebaseMessaging() {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
