package com.tools.seoultech.timoproject.auth.univ.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UnivVerification {

    @Id
    private String email;

    @Column(nullable = false)
    private String univName;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean isVerified = false;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public UnivVerification(String email, String univName, String code) {
        this.email = email;
        this.univName = univName;
        this.code = code;
        this.expiresAt = LocalDateTime.now().plusMinutes(10); // 10분 후 만료
    }

    public void updateCode(String newCode) {
        this.code = newCode;
        this.expiresAt = LocalDateTime.now().plusMinutes(10);
        this.isVerified = false;
    }

    public void verify() {
        this.isVerified = true;
    }
}