package com.tools.seoultech.timoproject.firebase;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fcm_tokens")
@Data
@NoArgsConstructor
public class FCMToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "token", nullable = false, length = 500)
    private String token;

    @Column(name = "device_type", length = 20)
    private String deviceType = "WEB";

    @Column(name = "is_active")
    private boolean isActive = true;

    public FCMToken(Long memberId, String token, String deviceType) {
        this.memberId = memberId;
        this.token = token;
        this.deviceType = deviceType;
        this.isActive = true;
    }
}

