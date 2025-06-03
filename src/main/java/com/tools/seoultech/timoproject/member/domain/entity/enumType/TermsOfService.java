package com.tools.seoultech.timoproject.member.domain.entity.enumType;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

// TermsOfService.java
@Getter
public enum TermsOfService {
    SERVICE(0, "서비스 이용약관", true),
    PRIVACY(1, "개인정보 수집·이용", true),
    SOCIAL_LOGIN(2, "소셜 로그인 이용", true),
    THIRD_PARTY(3, "제3자 정보 이용", true),
    COMMUNITY(4, "커뮤니티 이용규칙", true),
    MARKETING_EMAIL(5, "이메일 마케팅", false),
    MARKETING_PUSH(6, "푸시 마케팅", false),
    AGE_VERIFIED(7, "연령 인증", true);

    private final int position;
    private final String description;
    private final boolean required;

    TermsOfService(int position, String description, boolean required) {
        this.position = position;
        this.description = description;
        this.required = required;
    }

    // === 비트 마스크 관련 메서드 ===

    public int toBit() {
        return 1 << position;
    }

    // 단일 약관이 동의되었는지 확인
    public boolean isAgreedIn(int agreements) {
        return (agreements & toBit()) != 0;
    }

    // 약관 동의 추가
    public int addTo(int agreements) {
        return agreements | toBit();
    }

    // 약관 동의 제거
    public int removeFrom(int agreements) {
        return agreements & ~toBit();
    }

    // === 정적 유틸리티 메서드 ===

    // 여러 약관을 한번에 추가
    public static int addMultiple(int agreements, TermsOfService... terms) {
        for (TermsOfService term : terms) {
            agreements = term.addTo(agreements);
        }
        return agreements;
    }

    // 현재 동의한 약관 목록 가져오기
    public static Set<TermsOfService> getAgreedTerms(int agreements) {
        Set<TermsOfService> agreed = EnumSet.noneOf(TermsOfService.class);
        for (TermsOfService term : values()) {
            if (term.isAgreedIn(agreements)) {
                agreed.add(term);
            }
        }
        return agreed;
    }

    // 필수 약관이 모두 동의되었는지 확인
    public static boolean hasAllRequiredTerms(int agreements) {
        for (TermsOfService term : values()) {
            if (term.required && !term.isAgreedIn(agreements)) {
                return false;
            }
        }
        return true;
    }

    // 필수 약관 비트마스크 가져오기
    public static int getRequiredMask() {
        int mask = 0;
        for (TermsOfService term : values()) {
            if (term.required) {
                mask |= term.toBit();
            }
        }
        return mask;
    }

    // 마케팅 약관만 가져오기
    public static int getMarketingMask() {
        return MARKETING_EMAIL.toBit() | MARKETING_PUSH.toBit();
    }
}