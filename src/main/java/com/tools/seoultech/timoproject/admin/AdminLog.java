package com.tools.seoultech.timoproject.admin;

import com.tools.seoultech.timoproject.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class AdminLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false)
    private EntityType entityType; // Enum으로 로그 타입 관리

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false)
    private MethodType methodType; // Enum으로 로그 타입 관리

    private String targetId; // 대상 ID (예: 멤버 ID, 댓글 ID, 글 ID)
    private String performedBy; // 수행한 관리자 ID 또는 이름

    protected AdminLog() {
    } // JPA 기본 생성자

    public AdminLog(EntityType entityType, MethodType methodType, String targetId, String performedBy) {
        this.entityType = entityType;
        this.methodType = methodType;
        this.targetId = targetId;
        this.performedBy = performedBy;
    }

    // 로그 타입 관리용 Enum
    public enum EntityType {
        MEMBER, // 멤버 삭제
        COMMENT, // 댓글 삭제
        POST; // 글 삭제
    }
    public enum MethodType {
        POST, // 멤버 삭제
        DELETE, // 댓글 삭제
        PUT; // 글 삭제
    }
}
